package shamim.io

import com.rdc.importer.scrapian.ScrapianContext
import com.rdc.importer.scrapian.util.ModuleLoader

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
    final MAIN_URL = "https://www.ecfr.gov/current/title-15/subtitle-B/chapter-VII/subchapter-C/part-744/appendix-Supplement%20No.%204%20to%20Part%20744"
    def allEntity = []
    def entityNameList = []
    def entityAlias = []
    def aliasMap = [:]
    def addressMap = [:]

    def countWithAKA = 0
    def countwithountAKA = 1;
    def count = 1;

    BoiSecurities(context) {
        this.context = context
        ocrReader = moduleFactory.getOcrReader(context)
    }

    def initParsing() {
        def html = invoke(MAIN_URL)
        //println html
        invokeEntity(html)

        printMapValue()

        println "Scraping Final Data : " + aliasMap.size()
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

            entityAlias.clear()


            // -----Getting Name Only----- //
            if (!entity.contains("a.k.a")) {

                def entityMatcher = entity =~ /(?m)^.+?(?=[,])/
                if (entityMatcher) {

                    entityName = entityMatcher.group()
                    entityAddress = sanitizeAddress(entity, entityName)
                    println " ${countwithountAKA++}|EntityName: ${entityName} ======= EntityAlias : No Alias!======= EntityAddress : ${entityAddress}"


                } else {
                    //println entity
                }
            } else {

                // -----Getting Name and Alias ----- //
                //println entity
                def entityMatcher = entity =~ /(?m)^.+?(?=(?:\s*?<br>\s*?<br>))/
                if (entityMatcher) {
                    countWithAKA++
                    entityName = entityMatcher.group()
                    //println entityName
                    entityAddress = sanitizeAddress(entity, entityName)
                    (entityName, aliasList) = separateNameAndAliasWithAKA(entityName)

                    println " ${countwithountAKA++}|EntityName: ${entityName} ======= EntityAlias : ${aliasList} ======= EntityAddress : ${entityAddress}"

                } else {
                    //println "withount <br> : $entity"
                }
            }

        }
        println "count : ${count}"
    }

    def printMapValue() {
        //aliasMap.entrySet().each {entry-> println entry.key+"-------"+entry.value}
        //addressMap.entrySet().each {entry-> println entry.key+"--------"+entry.value}
//
        for (i in entityNameList) {
            //println i
        }
    }


    def sanitizeAddress(def entity, def entityName) {
        def tempAddress = entity.toString().replace(entityName, "")
        def tempMatcher;
        //println tempAddress
        tempAddress = tempAddress.replaceAll("<br>|<em>|</em>", "").trim()

        tempMatcher = tempAddress =~ /(?<=[,\.]\s).{3,}/
        if (tempMatcher) {
            tempAddress = tempMatcher.group()
            return tempAddress
        } else {
            return tempAddress = "No Address!"
        }

        return tempAddress

    }

    def sanitizeNameAndAlias(def nameAndAlias) {
        def tempAlias = "";
        def tempMatcher;
        tempMatcher = nameAndAlias =~ /(?<=,).+?(?=<br>)/
        if (tempMatcher) {
            tempAlias = nameAndAlias.replace(tempMatcher.group(), "")
        }

        tempAlias.trim()
        tempAlias = tempAlias.replaceAll(/<br>|<em>and<\/em>"/, "")
        tempAlias = tempAlias.replaceAll(/&amp;/, "&")


        return tempAlias
    }

    def separateNameAndAliasWithAKA(def nameAndAlias) {
        def tempAlias = "";
        def tempMatcher;
        def aliasList = []


        nameAndAlias = sanitizeNameAndAliasEntity(nameAndAlias)
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
            tempAliasList << i.trim()
        }
        return tempAliasList
    }

    def sanitizeNameAndAliasEntity(def nameAndAlias) {
        def tempNameAndAlias = nameAndAlias.toString()

        tempNameAndAlias = tempNameAndAlias.replaceAll(/Lukoil, OAO <br>/, "Lukoil, OAO")

        if (!tempNameAndAlias.contains("alias") && !tempNameAndAlias.contains("<br>")) {
            tempNameAndAlias = tempNameAndAlias.replaceAll(/(?i)(?:a\.k\.a.|f\.k\.a.)/, "a.k.a <br>")
            //println "inside: $tempNameAndAlias"
            return tempNameAndAlias
        }

        if (!tempNameAndAlias.contains("<br>")) {
            return tempNameAndAlias = tempNameAndAlias.replaceAll(/(?i)(?:alias|alises):/, "<br>")
        }

        return tempNameAndAlias
    }

    def sanitizeName(String name) {
        name = name.replaceAll(/,\s*$/, "").trim()
        //println name
        return name

    }


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

    //(?=.*a\.k\.a).*?(?ism)-.+?(?=[\.,;]) alias regex.

    //(?m)(?=.*(?:a\.k\.a)).*?(?:alias|aliases):\s*(-.+?(?=[\.,;])).*?(-.+?(?=[\.,;])) alias regex.
    // edited
}

