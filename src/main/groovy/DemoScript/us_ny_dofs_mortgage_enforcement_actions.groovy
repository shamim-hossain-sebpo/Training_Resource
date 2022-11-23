package shamim.io



import com.rdc.importer.scrapian.ScrapianContext
import com.rdc.importer.scrapian.model.StringSource
import com.rdc.importer.scrapian.util.ModuleLoader
import com.rdc.scrape.ScrapeAddress
import com.rdc.scrape.ScrapeEvent
//import me.afifaniks.parsers.TessPDFParser

context.setup([connectionTimeout: 500000, socketTimeout: 25000, retryCount: 1, multithread: true, userAgent: "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36"])
context.session.encoding = "UTF-8" //change it according to web page's encoding
context.session.escape = true

DofsMortgageEnforcement script = new DofsMortgageEnforcement(context)
script.initParsing()

//[For Dev]
//script.start()
//script.initWithTess()

class DofsMortgageEnforcement {

    def moduleLoaderVersion = context.scriptParams.moduleLoaderVersion
    final def moduleFactory = ModuleLoader.getFactory(moduleLoaderVersion)
    final entityType
    final ScrapianContext context
    final String urlInput = "https://www.dfs.ny.gov/industry_guidance/enforcement_actions_mortgage"
    final def urlRoot = "https://www.dfs.ny.gov"
    final def ocrReader
    final def ocrSpaceReader

//    TessPDFParser pdfParser = new TessPDFParser()
//    String text = pdfParser.convert("http://tdc-www.harvard.edu/Python.pdf",
//            [dpi: 150,
//             imageMode: "binary",
//             cache: false]);


    DofsMortgageEnforcement(context) {
        this.context = context
        entityType = moduleFactory.getEntityTypeDetection(context)
        ocrReader = moduleFactory.getOcrReader(context)
        ocrSpaceReader = moduleFactory.getOCRSpaceReader(context)
    }

    def initWithTess(){
        def pdfUrl = "https://www.oid.ok.gov/wp-content/uploads/2021/06/210604-Consent-Order-final.pdf"
        def pdfText = pdfToTextConverter(pdfUrl)
        //println(pdfText)
    }

    def start(def pdfUrl) {
        //def pdfUrl = "https://www.dfs.ny.gov/system/files/documents/2021/12/ea20211208_academy_mortgage_corp.pdf"
        //https://www.dfs.ny.gov/industry_guidance/enforcement_discipline/ea20211208_academy_mortgage_corp.pdf
        def pdfText = ocrSpaceReader.getText(pdfUrl)
        //println(pdfText)
        return pdfText
    }

    def initParsing() {
        def count = 0
        def html = invokeUrl(urlInput)
        def blockMatch = html =~ /(?ism)<table><tbody><tr><th width="15%">.+?,\s2020</
        if (blockMatch.find()) {
            def block = blockMatch.group(0)
            def rowMatch = block =~ /(?ism)<tr><td>.*?<\/tr>/
            while (rowMatch.find()) {
                count++
                //println "PDF : " + count
                def row = rowMatch.group(0)
                handleDetailsPage(row)
                //println()
            }
        }
    }

    def handleDetailsPage(row) {
        def pdfLink = ""
        def nameBlock
        def date
        def nameList = []

        def dateMatch = row =~ /(?ism)<tr><td>.+?<p>(.+?,\s*\d{4})<\/p>/
        def dateMatch1 = row =~ /(?ism)<tr><td>.+?<p><span>(.+?)<\/span><\/p>/
        if (dateMatch.find())
            date = dateMatch[0][1]

        if (dateMatch1.find())
            date = dateMatch1[0][1]

        def eDate = context.parseDate(new StringSource(date), ["MMM dd, yyyy"] as String[])
        //println eDate
        def edateMatcher = eDate =~ /(?i)(.*?)\/(.*?)\/(\d{4})/
        def urlDatePart
        if (edateMatcher.find()) {
            urlDatePart = edateMatcher[0][3] + "/" + edateMatcher[0][1] + "/"
        }
        //def pdfMatch = row =~ /(?i)<a\s*href="(.*?[^"](?>.pdf))"/
        def pdfMatch = row =~ /(?i)<a\s*href=".*?(ea2021.*?)">/
        if (pdfMatch.find()) {
            pdfLink = urlRoot + "/system/files/documents/" + urlDatePart + pdfMatch[0][1] + ".pdf"
            pdfLink = pdfLink.replaceAll(/\s+/, "%20")
            pdfLink = pdfLink.replaceAll(/residential_mortgage/, "residential_mortgage_0")
//            if(!pdfLink.toString().contains(".pdf")){
//                pdfLink = pdfLink + ".pdf"
//                println pdfLink
//            }
            if(pdfLink == "https://www.dfs.ny.gov/system/files/documents/2021/02/ea20210210_continental_mortgage_bankers.pdf"){
                pdfLink = "https://www.dfs.ny.gov/system/files/documents/2021/03/ea210210_continental_mortgage_bankers.pdf"
            }
            if(pdfLink == "https://www.dfs.ny.gov/system/files/documents/2021/02/ea20210210_first_guaranty_mortgage_corp.pdf"){
                pdfLink = "https://www.dfs.ny.gov/system/files/documents/2021/03/ea210210_first_guaranty_mortgage_corp_1.pdf"
            }
            if(pdfLink == "https://www.dfs.ny.gov/system/files/documents/2021/02/ea20210204_hunt_mortgage.pdf"){
                pdfLink = "https://www.dfs.ny.gov/system/files/documents/2021/02/ea20210204_hunt_mortgage_agreement.pdf"
                nameList.add("HUNT MORTGAGE CORPORATION")
            }
            //println pdfLink

            def finalText = ""
            List<String> pages = ocrSpaceReader.getText(pdfLink)
            pages.each {
                finalText += it
            }
            finalText = textFix(finalText)
            //println(finalText)

            def nameBlockMatch2 = finalText =~ /(?ism)in\s*the\s*matter\s*of\s*(.+?)[A-Z]*\d{3,7}/
            def nameBlockMatch3 = finalText =~ /(?ism)in\s*the\s*matter\s*of\s* SETTLEMENT AGREEMENT(.+?)\sA/
            def nameBlockMatch4 = finalText =~ /(?ism)- (vision property management.+?;.+?dek)\sand/

            if (nameBlockMatch3.find()) {
                nameBlock = sanitizeName(nameBlockMatch3[0][1])
            } else if (nameBlockMatch2.find()) {
                nameBlock = sanitizeName(nameBlockMatch2[0][1])
            } else if (nameBlockMatch4.find()) {
                nameBlock = sanitizeName(nameBlockMatch4[0][1])

            }
        }


        nameBlock?.split(" and |;|CORPORATION,").each {
            nameList.add(it)
        }
        createEntity(nameList, pdfLink, date)
    }

    def createEntity(nameList, pdfLink, date) {
        nameList.each {
            name ->
                if (name) {
                    name = name.toUpperCase()
                    def entity
                    def aliasList = []

                    def type = entityType.detectEntityType(name)
                    entity = context.findEntity([name: name, type: type])

                    if (!entity) {
                        entity = context.getSession().newEntity()
                        name.split("-ALIAS-").each {
                            aliasList.add(it)
                        }

                        def entityName = aliasList[0]
                        entity.setName(sanitize(entityName))
                        entity.setType(type)
                        aliasList.remove(0)
                    }

                    aliasList?.each {
                        entity.addAlias(sanitize(it))
                    }

                    entity.addUrl(pdfLink)

                    ScrapeAddress addressObj = new ScrapeAddress()
                    addressObj.setCountry("UNITED STATES")
                    addressObj.setProvince("New York")
                    entity.addAddress(addressObj)

                    def description = "The named entity was the subject of a Settlement Agreement enforcement action by the NY State Department of Financial Services."
                    def eDate = context.parseDate(new StringSource(date), ["MMM dd, yyyy"] as String[])
                    ScrapeEvent event = new ScrapeEvent()
                    event.setDate(eDate)
                    event.setDescription(description)
                    event.setCategory("REG")
                    event.setSubcategory("ACT")
                    entity.addEvent(event)
                }
        }
    }

    //------------------------------Filter part------------------------//

    def textFix(data) {

        data = data.replaceAll(/\s{2,}/, " ").replaceAll(/\r\n/, "\n").replaceAll(/\n/, " ")
        data = data.replaceAll(/(?i)(?<=agreement)(?=\w\d+)/, " ")

        return data
    }


    def detectEntityType(name) {
        def entityType = context.determineEntityType(name)
        return entityType
    }

    def sanitize(data) {
        return data.replaceAll(/\u200F/, "").replaceAll(/&apos;/, "'").replaceAll(/&amp;/, '&').replaceAll(/,\s*$/, "").replaceAll(/\r\n/, "\n").replaceAll(/(?s)\s{2,}/, " ").replaceAll(/(?ism), inc/, " INC").trim()
    }

    //------------------------------Misc utils part---------------------//

    def invokeUrl(url, isPost = false, isBinary = false, cache = false, postParams = [:], headersMap = [:], cleanSpaceChar = false, tidy = false, miscData = [:]) {
        Map data = [url: url, tidy: tidy, headers: headersMap, cache: cache, clean: cleanSpaceChar]
        data.putAll(miscData)
        return context.invoke(data)
    }

    def sanitizer(pdfurlblock) {
        pdfurlblock = pdfurlblock.replaceAll(/https:\/\/www.dfs.ny.gov/, "")
        return pdfurlblock
    }

    def sanitizeName(nameBlock) {
        nameBlock = nameBlock.replaceAll(/[A-Z]\d{6}/, "")
        nameBlock = nameBlock.replaceAll(/(?ism)SETTLEMENT.+?MENT/, "")
        nameBlock = nameBlock.replaceAll(/(?ism)SETTLEMENT/, "")
        nameBlock = nameBlock.toString().replaceAll(/(?ism)NMLS.+?\d{3,8}/, "")
        nameBlock = nameBlock.replaceAll(/CEASE/, "")
        nameBlock = nameBlock.replaceAll(/(?ism)no\./, "")
        nameBlock = nameBlock.replaceAll(/(?ism)NO\.#/, "")
        nameBlock = nameBlock.replaceAll(/(?ism)ID:/, "")
        nameBlock = nameBlock.replaceAll(/(?ism)NMLS #/, "")
        nameBlock = nameBlock.replaceAll(/(?ism)NMLS/, "")
        nameBlock = nameBlock.replaceAll(/(?ism)NMLS ID./, "")
        nameBlock = nameBlock.toString().replaceAll("\\(", "")
        nameBlock = nameBlock.toString().replaceAll("\\)", "")
        nameBlock = nameBlock.toString().replaceAll("ORDER OF REVOCATION", "")
        nameBlock = nameBlock.toString().replaceAll("x East", "East")
        nameBlock = nameBlock.toString().replaceAll(", j", ",")
        nameBlock = nameBlock.toString().replaceAll(", j", ",")
        nameBlock = nameBlock.toString().replaceAll(/(?i)(?:\b(dba|in lieu of|in lien of|ON BEHALF OF|formerly|d\/b\/a|n\.a\.\son.*?of)\b)/, "-ALIAS-")

        def p = nameBlock =~ /(.+?)\sA.+?Mortgage/
        def q = nameBlock =~ /(?ism)Investigation by.+?of.+?Services.+?of(.+?)Respondents/
        def r = nameBlock =~ /(?ism)(NATIONSTAR MORTGAGE.+?LLC)\s.+?-/
        def s = nameBlock =~ /(?ism)(NEW.+?)MARYLAND/
        def t = nameBlock =~ /(?ism)(SN.+?)\sx/
        def u = nameBlock =~ /(?ism)(OCWEN.+?)LLC\s-/
        def v = nameBlock =~ /(?ism)(OCWEN LOAN SERVICING.+?LLC) CONSENT ORDER UNDER/
        def w = nameBlock =~ /(?ism)(OCWEN FINANCIAL CORPORATION, OCWEN LOAN SERVICING, LLC)\sCONSENT\sORDER/
        def x = nameBlock =~ /(?ism)(MORTGAGE.+?LOANS),/
        def y = nameBlock =~ /(?ism)(PROSPECT.+?LLc)\sLimited/
        def z = nameBlock =~ /(?ism)(WELLS FARGO BANK.+?)Consent/

        if (z.find()) {
            nameBlock = z.group(1)
        } else if (p.find()) {
            nameBlock = p.group(1)
        } else if (q.find()) {
            nameBlock = q.group(1)
        } else if (r.find()) {
            nameBlock = r.group(1)
        } else if (s.find()) {
            nameBlock = s.group(1)
        } else if (t.find()) {
            nameBlock = t.group(1)
        } else if (u.find()) {
            nameBlock = u.group(1)
        } else if (v.find()) {
            nameBlock = v.group(1)
        } else if (w.find()) {
            nameBlock = w.group(1)
        } else if (x.find()) {
            nameBlock = x.group(1)
        } else if (y.find()) {
            nameBlock = y.group(1)
        }
        nameBlock = nameBlock.replaceAll(/(?ism)its/, "")
        nameBlock = nameBlock.replaceAll(/(?ism)CONSENT ORDER/, "")
        nameBlock = nameBlock.replaceAll(/(?ism)true.+?name/, "")
        nameBlock = nameBlock.replaceAll(/(?ism)n\.a\./, "")
        nameBlock = nameBlock.toString().replaceAll("(?ism)Mortgage Banker License", "")
        nameBlock = nameBlock.toString().replaceAll("(?ism)OCWEN FINANCIAL", "OCWEN FINANCIAL CORPORATION")
        nameBlock = nameBlock.toString().replaceAll(/(?ism)Residential Mortgage Services, Inc.+/, "Residential Mortgage Services, Inc.")

        return nameBlock
    }

    def pdfToTextConverter(def pdfUrl) {
        def pdfText = pdfParser.convert(pdfUrl, [dpi: 150, imageMode: "binary", cache: true, outputDir: "finraPdfText"])
        return pdfText
    }

}