package shamim.io

import com.rdc.importer.scrapian.ScrapianContext
import com.rdc.importer.scrapian.util.ModuleLoader
import com.rdc.scrape.ScrapeAddress

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

context.setup([connectionTimeout: 35000, socketTimeout: 45000, retryCount: 1, multithread: true, userAgent: "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36"])
context.session.encoding = "UTF-8" //change it according to web page's encoding
context.session.escape = true

BoiSecurities script = new BoiSecurities(context)
script.initParsing()

class BoiSecurities {
    final ScrapianContext context
    final def moduleFactory = ModuleLoader.getFactory("7dd0501f220ad1d1465967a4e02f6ff17d7d9ff9")
    //a2d905c86b1424c8656d0432d41029977e4f81cd
    final def ocrReader
    final addressParser
    final entityType
    final MAIN_URL = "https://www.ecfr.gov/current/title-15/subtitle-B/chapter-VII/subchapter-C/part-744/appendix-Supplement%20No.%204%20to%20Part%20744"
    def allEntity = []
    def entityNameList = []
    def entityAlias = []
    def countWithAKA = 0
    def countwithountAKA = 1;
    def count = 1;
    def i = 1;

    BoiSecurities(context) {
        this.context = context
        ocrReader = moduleFactory.getOcrReader(context)
        addressParser = moduleFactory.getGenericAddressParser(context)
        entityType = moduleFactory.getEntityTypeDetection(context)

        addressParser.updateCountries([ocl: ["Germany"],
        ])

        addressParser.updateCities([DE: ["Wermelskirchen"]

        ])
        addressParser.reloadData()
    }

    def initParsing() {
        def html = invoke(MAIN_URL)
        //println html
        invokeEntity(html)


        println "Total Entities ${entityNameList.size()}"
        println "Total Allentity ${allEntity.size()}"
        println "Entity With a.k.a : ${countWithAKA}"
        println "Entity Without a.k.a : ${countwithountAKA}"

    }


    def invokeEntity(def responseBody) {
        def entityData = responseBody =~ /(?is)<tr>.*?(?:<td.*?>(.*?)<\/td>\s*)(?:<td.*?>(.*?)<\/td>).+?<\/tr>/
        def count = 1;
        def entity = ""
        //def entityMatcher;
        def entityName;
        def entityAddress;
        def aliasList = [];

        while (entityData.find()) {
            count++
            // --- Getting entity ---- //
            entity = entityData.group(2)
            //println entity
            allEntity.push(entity)

            entity = mainEntitySanitize(entity)

            // -----Getting Name Only----- //
            if (!entity.contains("a.k.a") && !entity.contains("alias") && !entity.contains("f.k.a")) {
                aliasList.clear()
                entity = sanitizeEntityWithoutAKA(entity)
                def entityMatcher = entity =~ /(?m)^.+?(?=[,])/
                if (entityMatcher) {

                    entityName = entityMatcher.group()
                    entityAddress = sanitizeAddress(entity, entityName)
                    //aliasList << "No Alias !"
                    createEntity(sanitizeName(entityName), aliasList, entityAddress)
                    //println " ${countwithountAKA++}|EntityName: ${entityName} ======= EntityAlias : ${aliasList}======= EntityAddress : ${entityAddress}"

                    // println "${i++}| Entity Address : $entityAddress"


                } else {
                    //println entity
                }
            } else {
                //if (entity.contains("f.k.a")) println "${i++} | $entity"

                // -----Getting Name and Alias ----- //
                //println entity
                entity = sanitizeEntityWithAKA(entity)
                def entityMatcher = entity =~ /(?m)^.+?(?=(?:\s*?<br>\s*?<br>))/
                if (entityMatcher) {
                    countWithAKA++
                    entityName = entityMatcher.group()
                    //println entityName
                    entityAddress = sanitizeAddressWithAKA(entity, entityName)
                    (entityName, aliasList) = separateNameAndAliasWithAKA(entityName)
                    createEntity(sanitizeName(entityName), aliasList, entityAddress)
                    //println "${i++}| Entity Address : $entityAddress"


                    //println " ${countwithountAKA++}|EntityName: ${entityName} ======= EntityAlias : ${aliasList} ======= EntityAddress : ${entityAddress}"

                } else {
//                    if (entity.contains("a.k.a") && !entity.contains("<br>"))
                    println "${i++}| $entity"
                }
            }

        }
        println "count : ${count}"
    }

    def mainEntitySanitize(def mainEntity) {
        mainEntity = mainEntity.toString().replaceAll(/(U\.A\.E)/, 'United Arab Emirates')
        mainEntity = mainEntity.toString().replaceAll(/(?i)(\(See\s(?:alternate|also).+?\))/, "")
        mainEntity = mainEntity.toString().replaceAll(/(?i)(.+?Ministry.+?Republic.+?Belarus),(.+?Wherever located)(.+)/, '$1($2),Belarus')
        mainEntity = mainEntity.toString().replaceAll(/(?i)(Ministry.+?Federation),(.+?Wherever located)(.+)/, '$1($2),Russia')
        mainEntity = mainEntity.toString().replaceAll(/(?i)(Slovakia)/, 'Slovak Republic')
        return mainEntity
    }

    def sanitizeEntityWithoutAKA(def entity) {
        //println println("WithoutAKA:  $entity")
        entity = entity.toString().replaceAll(/(?i)(Co\.,?|Company,?)(?:\s?Ltd\.|Limited)/, "Co. Ltd.,")
        entity = entity.toString().replaceAll(/&amp;/, "&")
        entity = entity.toString().replaceAll(/China Aerodynamics Research and Development Center \(CARDC\)\./, '$0,')
        entity = entity.toString().replaceAll(/Ali Mehdipour Omrani\./, '$0, Iran')
        entity = entity.toString().replaceAll(/Aref Bali Lashak\./, '$0, Iran')
        entity = entity.toString().replaceAll(/Kamran Daneshjou\./, '$0, Iran')
        entity = entity.toString().replaceAll(/Mehdi Teranchi\./, '$0, Iran')
        entity = entity.toString().replaceAll(/Sayyed Mohammad Mehdi Hadavi./, '$0, Iran')
        entity = entity.toString().replaceAll(/^\s?Allied Trading Co\s?$/, '$0, Pakistan')
        entity = entity.toString().replaceAll(/^Prime International$/, '$0, Pakistan')
        entity = entity.toString().replaceAll(/^Unique Technical Promoters$/, '$0, Pakistan')
        entity = entity.toString().replaceAll(/Juba Petrotech Technical Services Ltd\./, '$0,')
        entity = entity.toString().replaceAll(/Safinat Group\./, "Safinat Group,")
        entity = entity.toString().replaceAll(/(?i)^(Brian Douglas Woodford)/, '$1,United Kingdom')
        entity = entity.toString().replaceAll(/(?i)(Academy of Military Medical Sciences),(\s.+?)(?=,)/, '$1($2)')

        println println("WithoutAKA:  $entity")
        return entity
    }

    def sanitizeEntityWithAKA(def entity) {
        println entity
        entity = entity.toString().replaceAll(/&amp;/, "&")
        entity = entity.toString().replaceAll(/Magtech, a\.k\.a., the following one alias: <br> - M\.A\.G\. Tech,/, '$0 <br> <br>')
        entity = entity.toString().replaceAll(/-MEO GMBH/, '$0.')
        entity = entity.toString().replaceAll(/one alias: <br> - Mujahid Ali Mahmood Ali/, '$0.')
        entity = entity.toString().replaceAll(/Institute of Physics Named After P.N. Lebedev of the Russian Academy of Sciences, a.k.a., the following four aliases: <br> - Lebedev Physical Institute; <br> - LPI RAS; <br> - Lebedev Physical Institute; <em>and<\/em> <br> - FIAN/, '$0<br> <br> 53 Leninsky Prospekt, Moscow, 119991, Russia.')
        entity = entity.toString().replaceAll(/Institut Problem Peredachi Informatsii RAN./, '$0<br> <br> Russia.')
        entity = entity.toString().replaceAll(/(?i)(Jamal\sH.+?ji,)(\s<br>\sNo\.\s102)/, '$1 <br>$2')
        entity = entity.toString().replaceAll(/(?i)(LTS\sHolding\sLI.+?ts\sLtd.+?,)/, '$1<br> <br>')
        entity = entity.toString().replaceAll(/(?i)(Rosneft\sTra.+?TNK\sTra.+?ed.)/, '$1<br> <br>')

        //------- Multiple alias Sanitize Entity -----------
        entity = entity.toString().replaceAll(/(?i)(a\.k\.a.?),(.+?UM\sA.+?,)(.+?r,)/, '$1<br>$2<br>$3 <br> <br>')
        entity = entity.toString().replaceAll(/(?i)(a\.k\.a.?),(.+?Hassan.+?Co\.),(.+?Electronics),(.+?Street),/, '$1<br>$2<br>$3<br>$4 <br> <br>')
        entity = entity.toString().replaceAll(/(?i)(a\.k\.a.?),(.+?\(ISSAT\)),(.+?\(ISAT\)),/, '$1<br>$2<br>$3 <br> <br>')
        entity = entity.toString().replaceAll(/(?i)(a\.k\.a.?),(.+?\(IED\)),(.+?\(EID\)),(.+?\(ETINDE\))/, '$1<br>$2<br>$3<br>$4 <br> <br>')
        entity = entity.toString().replaceAll(/(?i)(a\.k\.a.?),(.+?\(SSRC\)-NSCL),(.+?Centre),/, '$1<br>$2<br>$3 <br> <br>')
        entity = entity.toString().replaceAll(/(?i)(a\.k\.a.?),(.+Scientific.+?,)(.+?,)(.+?,)(.+?,)(.+?,)(.+?,)(.+\(SRC\),)/, '$1<br>$2<br>$3<br>$4<br>$5<br>$6<br>$7<br>$8 <br> <br>')
        entity = entity.toString().replaceAll(/(?i)(a\.k\.a.?,?)(.+?Gill),(.+?dian),/, '$1<br>$2<br>$3 <br> <br>')

        entity = entity.toString().replaceAll(/(?i)(54th.+?Institute.+?<br>\s-\sShijiazhuang.+?tute)\./, '$1, China.')


        if (entity.contains("aliases") && !entity.contains("alias:") && !entity.contains("<br> <br>") && entity.contains("<br>")) {
            entity = entity.toString().replaceAll(/(?i).+<br>.+?(?:\.\s|,\s)/, '$0<br> <br>')
            // println "${i++} | $entity"
            return entity
        }

        if (entity.contains("one alias") && !entity.contains("aliases") && !entity.contains("<br> <br>")) {
            entity = entity.toString().replaceAll(/(?i)one alias:?.+?(?:Co\.,?\s?Ltd)?[\.,]/, '$0 <br> <br>')
            //println "${i++} | $entity"
            return entity
        }

        if (entity.contains("two aliases") && !entity.contains("<br>")) {
            entity = entity.toString().replaceAll(/(?i)two aliases:?.+?(?:Co\.,?\s?Ltd)?[\.,]/, '$0 <br> <br>')
            //println "${i++} | $entity"
            return entity
        }

        if (entity.contains("a.k.a") && !entity.contains("<br>")) {
            //entity = entity.toString().replaceAll(/(?i)(\(a\.k\.a.?.+?(?:\)\.|\),))/, '$0<br> <br>')
            entity = entity.toString().replaceAll(/(?i)(a\.k\.a.?.+?),/, '$1<br> <br>')


            // println "${i++} |${entity}"

            return entity
        }
        return entity
    }

    def sanitizeAddress(def entity, def entityName) {
        //println entity
        def tempAddress = entity.toString().replace(entityName, "")
        def tempMatcher;
        //println tempAddress

        tempAddress = tempAddress.replaceAll(/<br>|<em>|<\/em>/, "")
        tempAddress = tempAddress.replaceAll(/(?i)(Province 211100)(;\sand.+?188.+?100)(;\sand.+?31.+?1201)(;\sand.+?jun\sAv.+?Nanjing)/, '$1,China $2,China $3,China $4,China')
        tempAddress = tempAddress.replaceAll(/(?i)(Development Zone)(;\sand.+Hunan Province)(;\sand.+410221)/, '$1,China $2,China $3,China')
        tempAddress = tempAddress.replaceAll(/(?i)(Quanzhou, Fujian)(.+District, Shanghai)(;\sand.+District, Chengdu)(;\sand.+District, Wuhan)(;\sand.+District, Luoyang)(;\sand.+District, Hefei)(;\sand.+Zone)/, '$1,China $2,China $3,China $4,China $5,China $6,China $7,China')
        tempAddress = tempAddress.replaceAll(/(?i)(Shaanxi)(;\sand.+Shenzhen City)(;\sand.+Zhongshan City)(;\sand.+Zhejiang Province)/, '$1,China $2,China $3,China $4, China')
        tempAddress = tempAddress.replaceAll(/(?i)(19,.+?Savitaipale)/, '$1,Finland')
        tempAddress = tempAddress.replaceAll(/(?i)(P\.O\..+42908)\sDE(;\sand.+42908)\sDE/, '$1,Germany $2,Germany')
        tempAddress = tempAddress.replaceAll(/(?i)(22,.+Moscow\s127055Ru)/, '$1,Russia')
        tempAddress = tempAddress.replaceAll(/(?i)(in.+?Okhotsk)/, '$1,Russia')
        tempAddress = tempAddress.replaceAll(/(?i)(1A.+?Zhubei.+?30274)/, '$1,Taiwan')

        tempAddress = tempAddress.replaceAll(/(?i)(shops.+?Emirates\.;\s)(P\..+?Emirates\.)/, 'and$1and$2;')


        tempMatcher = tempAddress =~ /^,,?\s?(.+)/
        if (tempMatcher) {
            tempAddress = tempMatcher.group(1)
        } else {
            tempAddress = "No Address!"
        }
        //println tempAddress
        return tempAddress.trim()
    }


    def sanitizeAddressWithAKA(def entity, def entityName) {



        def tempAddress = entity.toString().replace(entityName, "")
        tempAddress = tempAddress.replaceAll(/<br>|<em>|<\/em>/, "")
        tempAddress = tempAddress.replaceAll(/(?i)(?:Alt)?\s?Address[es]*:?/, "")

        //println tempAddress

        tempAddress = tempAddress.replaceAll(/(?i)(Luz.+?ment)(.+\\/span>)/, "")


        //-------- Missing Country (Mostly China)----------//
        tempAddress = tempAddress.replaceAll(/(?i)(Xueyan.+?Beijing.+?Ning\sBuilding,.+?)\./, '$1,China.')
        tempAddress = tempAddress.replaceAll(/(?i)(No\..+?Changji.+?Hills\))(;\sand.+?Oasis.+?)\./, '$1,China$2,China.')
        tempAddress = tempAddress.replaceAll(/(?i)(Building\s9.+?Yuejin.+?Free)(;\sand.+?Hexi.+?)\./, '$1,China$2,China.')
        tempAddress = tempAddress.replaceAll(/(?i)(198\sAicheng.+?Hangzhou)(;\sand.+?Macheng.+?Hangzhou)(;\sand.+?Wenyi.+?District)(;\sand.+?Qingha.+?Hangzhou)/, '$1,China$2,China$3,China$4,China')
        tempAddress = tempAddress.replaceAll(/(?i)(64\sMianshan.+?11\sJindu.+?8\sHuayuan.+?Beijing)(;\sand.+?Beijing)/, '$1,China$2,China')
        tempAddress = tempAddress.replaceAll(/(?i)(Xicheng.+?Shaanxi\sProvince)/, '$1,China')
        tempAddress = tempAddress.replaceAll(/(?i)(Nanhu.+?Wuhan\sC.+?Province)/, '$1,China')
        tempAddress = tempAddress.replaceAll(/(?i)(Building.+?Hangzhou\sC.+?Province)(;\sand.+?209\sGold.+?Zhejiang)/, '$1,China$2,China')
        tempAddress = tempAddress.replaceAll(/(?i)(.+?Anhui\sProvince)\./, '$1,China.')
        tempAddress = tempAddress.replaceAll(/(?i)(88\sHengtong.+?Wujiang.+?Jiangsu\sProvince)/, '$1,China')
        tempAddress = tempAddress.replaceAll(/(?i)(No\.\s88.+?Yaoguan.+?Changzhou\sCity)\./, '$1,China')
        tempAddress = tempAddress.replaceAll(/(?i)(No\.\s18\sWest.+?Security,\sXicheng.+?uke\sMuni.+?)\./, '$1,China.')
        tempAddress = tempAddress.replaceAll(/(?i)(6\sDongsheng.+?A8-4.+?214437)\./, '$1,China.')
        tempAddress = tempAddress.replaceAll(/(?i)(Room\s602,.+?\.158.+?Park)\./, '$1,China.')
        tempAddress = tempAddress.replaceAll(/(?i)(?i)(GF\sSeapower.+?Hong\sKong)(,\sand)(\sRoom.+?Hong\sKong)(,\sand)(.+?om,\skowloon)\./, '$1; and$3; and$5,Hong Kong.')
        tempAddress = tempAddress.replaceAll(/(?i)(Shihezi\sDev.+?Park)(;\sand.+)/, '$1,China$2')
        tempAddress = tempAddress.replaceAll(/(?i)(Wucaiwan.+?Changji\sPrefecture.+?ity\))(;\sand.+)/, '$1,China$2')
        tempAddress = tempAddress.replaceAll(/(?i)(East.+?Quanbei\sI.+?Prefecture.+?ity\))(;\sand.+)/, '$1,China$2')
        tempAddress = tempAddress.replaceAll(/(?i)(Xinjiang\sur.+?No\.258\sGaox.+?891)\./, '$1,China.')
        tempAddress = tempAddress.replaceAll(/(?i)(Room\s24.+?ce,\sChina;\s)(Room\s18.+?an,\sChina)/, '$1and$2')

        tempAddress = tempAddress.replaceAll(/(?i)(No\.\s40-.+?Iran;\sand.+)\./, '$1,Iran.')
        tempAddress = tempAddress.replaceAll(/(?i)(?:\(Add.+?)(P\.O\..+?Russia)\)(;\sand.+?entities.+?Snez.+?)\./, '$1$2,Russia.')
        tempAddress = tempAddress.replaceAll(/(?i)(?:\(Add.+?)(37\sMira.+?\d{6}\sRussia)\)(;\sand.+?entities.+?Kremlev.+?)\./, '$1$2,Russia.')
        tempAddress = tempAddress.replaceAll(/(?i)(2\sFatkulina.+?Tartarstan.+?21)\./, '$1,Russia.')
        tempAddress = tempAddress.replaceAll(/(?i)(9A\s2nd.+pol,\sst.+037)\./, '$1,Russia.')
        tempAddress = tempAddress.replaceAll(/(?i)(40\sMoskovsky.+?lic,\s\d{6})(;\sand\s7.+)/, '$1,Russia$2')
        tempAddress = tempAddress.replaceAll(/(?i)(25\sBol.+?kaya\sst.+\d{6})(;\sand.+)/, '$1,Russia$2')
        tempAddress = tempAddress.replaceAll(/(?i)(33,\sGa.+?Gagarin.+?ul\..+?obl\s\d{6})\./, '$1,Russia')
        tempAddress = tempAddress.replaceAll(/(?i)(lit\sA.+?ulitsa,.+?\d{6})\./, '$1,Russia')
        tempAddress = tempAddress.replaceAll(/(?i)(4\sPokhodnyy.+?;\sand\s46.+?\d{6})\./, '$1,Russia')
        tempAddress = tempAddress.replaceAll(/(?i)(Pr\..+?;\sand\s5.+?\d{6})\./, '$1,Russia')

        tempAddress = tempAddress.replaceAll(/(?i)(Alt\sAddress:)/, '; and')
        tempAddress = tempAddress.replaceAll(/(?i)(Suite\s801.+?8\sCa.+?Beijing)(;\sand.+?hai\s\d{6})(;\sand.+?Tian.+?\d{6})(;\sand.+?an,\s\d{6})(;\sand.+?gdu,\s\d{6})(;\s)(.+?gdu\s\d{6})(;\sand.+?zhou,\s\d{6})(;\sand.+?hang,\s\d{6})/, '$1,China$2,China$3,China$4,China$5,China$6and$7,China$8,China$9,China')
        tempAddress = tempAddress.replaceAll(/(?i)(Bud\.\s9.+?str\.,\sV.+?433,\s)and(.+)/, '$1$2')
        tempAddress = tempAddress.replaceAll(/(?i)(Let\..+?ya\,\s5.+?ssia)(.+?burg\s19.+?ia)(.+?ya\sSt.+?ia)/, '$1; and$2; and$3; and')


        //println tempAddress
        return tempAddress.trim()
    }

    def sanitizeNameAndAlias(def nameAndAlias) {
        def tempAlias = "";
        def tempMatcher;
        tempMatcher = nameAndAlias =~ /(?<=,).+?(?=<br>)/
        if (tempMatcher) {
            tempAlias = nameAndAlias.replace(tempMatcher.group(), "")
        }
        tempAlias.trim()
        tempAlias = tempAlias.replaceAll(/<br>|<em>|<\/em>"/, "")
        return tempAlias
    }

    def separateNameAndAliasWithAKA(def nameAndAlias) {
        def tempAlias = "";
        def tempMatcher;
        def aliasList = []
        def tempAliasFkaList = []


        nameAndAlias = sanitizeNameAndAliasEntity(nameAndAlias)
        if (nameAndAlias.contains("Pakistan Atomic Energy Commission (PAEC)")) println "Pakistan Multiple alias : $nameAndAlias"
        //println " ${count++} | $nameAndAlias"

        //def nameList = nameAndAlias.toString().split(/(?i)a\.k\.a.+?(?:Alias|Aliases):/).collect { it }
        def nameList = nameAndAlias.toString().split(/(?i)a\.k\.a.+?(?=<br>)/).collect { it }
        //println "nameList ${nameList}"
        def name = nameList[0]

        if (nameList.size() > 2) {
            nameList.remove(0)
            aliasList = nameList
        } else {
            if (nameList[1] != null && nameList[1].contains("<br>")) {
                aliasList = nameList[1].split(/<br>/).collect { it }
            } else {
                println "Null Value : $nameList"
            }
        }

        if (name.contains("f.k.a")) {
            tempAliasFkaList = name.toString().split(/\(?f\.k\.a\.,?/).collect { it }
            name = tempAliasFkaList[0]
            tempAliasFkaList.remove(0)
            tempAliasFkaList.each {aliasList.add(it)}
        }
        //println "Name : ${name} ======== AliasList : $aliasList"
        aliasList = sanitizeAliasList(aliasList)

        return [name, aliasList]
    }


    def sanitizeAliasList(def aliasList) {
        def tempAliasList = []
        for (i in aliasList) {
            i = i.toString().replaceAll(/^.+?(?=\w)/, "")
            i = i.toString().replaceAll(/<em>and<\/em>/, "")
            i = i.toString().replaceAll(/&amp;/, "&")
            i = i.toString().replaceAll(/(?i)(and\s?$)/, "")
            i = i.toString().replaceAll(/(?s)\s+/, " ")

            tempAliasList << i.trim()
        }
        return tempAliasList
    }

    def sanitizeNameAndAliasEntity(def nameAndAlias) {

        def tempNameAndAlias = nameAndAlias.toString()
        tempNameAndAlias = tempNameAndAlias.replaceAll(/Lukoil, OAO <br>/, "Lukoil, OAO")
        tempNameAndAlias = tempNameAndAlias.replaceAll(/(?i)(\s\(a\.k\.a\.,\sChina.+?\(CAEP\).+?\))(.+?Mechanics;)(\s\(all.+?\))/, '$2')
        tempNameAndAlias = tempNameAndAlias.replaceAll(/(?i)(.+?rd.+?iv.+?f\.k\.a.+?iv');(\sJSC.+?iv;')(.+?ard).+?\\/em>/, '$1 f.k.a. $2 f.k.a. $3 f.k.a.')
        tempNameAndAlias = tempNameAndAlias.replaceAll(/(?i)(.+?ost\s-\sSai.+?f\.k\.a..+?ZAO);.+?\\/em>/, '$1 f.k.a.')

        if (!tempNameAndAlias.contains("alias") && !tempNameAndAlias.contains("<br>")) {
            tempNameAndAlias = tempNameAndAlias.replaceAll(/(?i)(?:a\.k\.a.|f\.k\.a.)/, "a.k.a <br>")
            //println "inside: $tempNameAndAlias"
            return tempNameAndAlias
        }

        if (tempNameAndAlias =~ /(?i)(a\.k\.a.+?alias.+?f\.k\.a)/) {
            tempNameAndAlias = tempNameAndAlias.replaceAll(/(?i)(\(?f\.k\.a.,?)/, "<br>")
            //println "inside : $tempNameAndAlias"
        }

        if (!tempNameAndAlias.contains("<br>")) {
            return tempNameAndAlias = tempNameAndAlias.replaceAll(/(?i)(?:alias|aliases):/, "<br>")
        }

        if (tempNameAndAlias.contains("alias") && !tempNameAndAlias.contains("a.k.a")) {
            return tempNameAndAlias = tempNameAndAlias.replaceAll(/(?i)(.+?,)(\s.+?)((?:alias|aliases))/, '$1a.k.a $3')
            // println "New Entity : $tempNameAndAlias"
        }
        return tempNameAndAlias
    }

    def sanitizeName(String name) {
        def tempName = name.toString()
        tempName = tempName.replaceAll(/(?i)(.+),\s*$/, '$1')
        tempName = tempName.replaceAll(/(?i)(.+)\(\s*$/, '$1')
        tempName = tempName.replaceAll(/(?i)(.+),\s*$/, '$1')
        tempName = tempName.replaceAll(/(?s)\s+/, " ")

        //println name
        return tempName.trim()
    }


    //------------- Create Entity ----------------- //
    def createEntity(def name, def aliasList, def address) {

        def entity;
        if (name != null) {

            entity = context.findEntity([name: name])

            if (entity == null) {
                entity = context.getSession().newEntity()
                entity.setName(name)
                def type = entityType.detectEntityType(name)
                entity.setType(type)

                address.split("; and").each { ad ->
                    ad = sanitizeAddresss(ad)
                    def addrMap = addressParser.parseAddress([text: ad, force_country: true])
                    ScrapeAddress scrapeAddress = addressParser.buildAddress(addrMap)
                    if (scrapeAddress) {
                        entity.addAddress(scrapeAddress)
                    }
                }
                aliasList.each {
                    if (it) {
                        entity.addAlias(it)
                    }
                }

            }
        }


    }

    String sanitizeAddresss(String rawFormat) {
        //format Address to : lane address,postal code, city, province, country
        return rawFormat.replaceAll(/(?s)\s+/, " ")
    }

    //https://regex101.com/r/uzzT4k/1 (alias)


//------------------------------Misc utils part---------------------//
    def invoke(url, cache = false, tidy = false, headersMap = [:], miscData = [:]) {
        Map dataMap = [url: url, tidy: tidy, headers: headersMap, cache: cache]
        dataMap.putAll(miscData)
        return context.invoke(dataMap)
    }


    //=========defaults===================
    def downloadImage(def imageLink, def path) {
        InputStream inn = new URL(imageLink).openStream()
        Files.copy(inn, Paths.get(path), StandardCopyOption.REPLACE_EXISTING)
    }

    def pdfToTextConverter(def pdfUrl) {
        try {
            List<String> pages = ocrReader.getText(pdfUrl)
            return pages
        } catch (NoClassDefFoundError e) {
            def pdfFile = invokeBinary(pdfUrl)
            def pmap = [:] as Map
            pmap.put("1", "-layout")
            pmap.put("2", "-enc")
            pmap.put("3", "UTF-8")
            pmap.put("4", "-eol")
            pmap.put("5", "dos")
            //pmap.put("6", "-raw")
            def pdfText = context.transformPdfToText(pdfFile, pmap)
            return pdfText
        }
        catch (IOException e) {
            return "PDF has no page"
        }
        catch (Exception e) {
            Thread.sleep(10000)
            return "-- RUNTIME ERROR --- $e.message ---"
        }
    }

    def invokeBinary(url, type = null, paramsMap = null, cache = false, clean = true, miscData = [:]) {
        //Default type is GET
        Map dataMap = [url: url, type: type, params: paramsMap, clean: clean, cache: cache]
        dataMap.putAll(miscData)
        return context.invokeBinary(dataMap)
    }


}

