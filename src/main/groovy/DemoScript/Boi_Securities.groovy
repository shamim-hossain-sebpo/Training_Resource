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
    def entity;
    def allEntity = []
    def entityName = []
    def entityAlias = []
    def entityAddres = []
    def aliasMap = [:]
    def addressMap = [:]

    BoiSecurities(context) {
        this.context = context
        ocrReader = moduleFactory.getOcrReader(context)
    }

    def initParsing() {
        def html = invoke(MAIN_URL)
        //println html
        invokeEntity(html)

        //----- printing All Info and Alias----
        def data = aliasMap.entrySet();
        data.each { entry -> println entry.key + "-----" +  entry.value
        }

//        for(i in entityName){
//            println i
//        }

        println aliasMap.size()
        println "Total Entities ${entityName.size()}"
        println "Total Allentity ${allEntity.size()}"

    }


    def invokeEntity(def responseBody) {
        def entityData = responseBody =~ /(?is)<tr>.*?(?:<td.*?>(.*?)<\/td>\s*)(?:<td.*?>(.*?)<\/td>).+?<\/tr>/
        def count = 1;
        def entityMatcher;

        while (entityData.find()) {
            count++
            // --- Getting entity ---- //
            entity = entityData.group(2)
             println entity
            allEntity.push(entity)

            entityAlias.clear()

            // -----Getting Name and Alias ----- //
            if (!entity.contains("a.k.a")) {
                entityMatcher = entity =~ /(?m)^.+?(?=,)/
                if (entityMatcher) {
                    entityName.push(entityMatcher.group())
                    aliasMap.put(entityMatcher.group(),"No Alias!")
                    addressMap.put(entityMatcher.group(),sanitizeAddress(entity, entityMatcher.group()))
                }
            } else {
                // -----Getting Name and Alias ----- //
                entityMatcher = entity =~ /(?m)^.+?(?=(?:\s*?<br>\s*?<br>))/
                if (entityMatcher) {
                    entityName.push(entityMatcher.group())
                    String[] arrayNameAndAlias = sanitizeNameAndAlias(entityMatcher.group()).split("-")

                    for (int i = 1; i < arrayNameAndAlias.length; i++) {
                        entityAlias.push(arrayNameAndAlias[i])
                    }

                    aliasMap.put(arrayNameAndAlias[0], entityAlias)
                    //println " entityAliasList :  $entityAlias"


                    addressMap.put(arrayNameAndAlias[0],sanitizeAddress(entity, entityMatcher.group()))
                }
            }

        }
        println "count : ${count}"
    }


    def sanitizeAddress(def entity, def entityName) {
        def tempAddress = entity.replace(entityName, "");
        def tempMatcher;
        tempAddress = tempAddress.replace("<br>", "")
        tempAddress = tempAddress.replace("<em>", "")
        tempAddress = tempAddress.replace("</em>", "")

        tempMatcher = tempAddress =~ /(?<=,\s).+/
        if (tempMatcher) {
            tempAddress = tempMatcher.group()
        }

        return tempAddress

    }

    def sanitizeNameAndAlias(def nameAndAlias) {
        def tempAlias="";
        def tempMatcher;
        tempMatcher = nameAndAlias =~ /(?<=,).+?(?=<br>)/
        if (tempMatcher) {
            tempAlias = nameAndAlias.replace(tempMatcher.group(), "")
        }

        tempAlias = tempAlias.replace("<br>", "")
        tempAlias = tempAlias.replace("<em>and</em>", "")


        return tempAlias
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

