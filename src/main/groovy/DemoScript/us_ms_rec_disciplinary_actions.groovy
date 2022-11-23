package shamim.io


import com.rdc.importer.scrapian.ScrapianContext
import com.rdc.importer.scrapian.model.StringSource
import com.rdc.importer.scrapian.util.ModuleLoader
import com.rdc.scrape.ScrapeAddress
import com.rdc.scrape.ScrapeEvent
import com.rdc.scrape.ScrapeIdentification

context.setup([connectionTimeout: 20000, socketTimeout: 25000, retryCount: 1, multithread: true, userAgent: "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36"])
context.session.encoding = "UTF-8" //change it according to web page's encoding
context.session.escape = true

Us_ms_rec script = new Us_ms_rec(context)
script.initParsing()
class Us_ms_rec {
    final entityType
    ScrapianContext context = new ScrapianContext()
    final def ocrReader
    def moduleLoaderVersion = context.scriptParams.moduleLoaderVersion
    final def moduleFactory = ModuleLoader.getFactory(moduleLoaderVersion)

    Us_ms_rec(context) {
        this.context = context
        entityType = moduleFactory.getEntityTypeDetection(context)
        ocrReader = moduleFactory.getOcrReader(context)

    }

    def initParsing() {
        def url = "http://www.mrec.ms.gov/disciplinary.html"
        def htmlFormData = invokeGET(url)
        handleDetailsPage(htmlFormData)
    }

    def handleDetailsPage(def htmlFormData) {

        def tableData
        def tableMacher = htmlFormData =~ /(?ism)\d{4}\s*MREC DISCIPLINARY ACTIONS.+?<\/table>/
        while (tableMacher.find()) {
            tableData = tableMacher.group()
            def year
            def yearMatcher = tableData =~ /(?ism)\">(\d{4})\s*MREC\sDISCIPLINARY\sACTIONS</
            if (yearMatcher.find()) {
                year = yearMatcher.group(1)
            }

            def pdfLink

            def pdfMacher = tableData =~ /(?ism)<a\s*href=\"(.+?\.pdf)\">(.+?)<\//
            while (pdfMacher.find()) {
                pdfLink = pdfMacher.group(1)

                pdfLink = "http://www.mrec.ms.gov/" + pdfLink
//                pdfLink = "http://www.mrec.ms.gov/documents/ADAMSTANJAELIZABETH.pdf"

                def pdfData = pdfToTextConverter(pdfLink)
                def name
                def temNameList = []
                if (pdfData =~ /(?ism)(?:(?:IO|No|CASE)(?:\.|\s*\#|\s*\_)\s*(?!ON)[A-Z\d\:]+[\-\w\:]+\d(?!\s*\d*?\s+AFFIDAVIT|\d*\-|\d*\s*AGREED|\-?\d*\s*RESPONDENT|\d*?\s*\,\s|\d*?\.\s)|\sv\.(?!\s*Douglas|\,|\s*In\s*|\s*As\s|\s*[\w]+\sdid|\s*Mohamed|\s*A formal|\s*Of|\s*Broker|\s*Based|\s*Af.er|\s*Gooch|\s*Stag|\s*Dissatistied|\s*Respondent|\s*Right|\s*At\sthe|\s*\-fhe|\s*Included|\s*All\sof|\s*on|\s*Among|\s*Upon|\s*The)|vs(?:\.|\,)\s*(?!NO|RESP|\s*CASE|\s*[\w\.\,\-]+\s*Respondent|COMP|\,))\s*(.+?)(?:(?<!NO\.\d{2}\-\d{4}\s)RESPONDENT|MS\s39047|(?:A\(\;Rh\sIll\)|AGRIED|AGRFEIT|AGBT\]ED|AGzulED|AGREEI\)|AGREED|ACREED)\s*(?:\(\)l\{l\)llR|ORI\)ER|ORpnR|ORDER|ORDEII)|\.\\\(\;REII\)\sORI\)t\,\)l|(?<!AGREED\s)ORDER(?!\s*to)|DEAR)/) {
                    def nameMacher = pdfData =~ /(?ism)(?:(?:IO|No|CASE)(?:\.|\s*\#|\s*\_)\s*(?!ON)[A-Z\d\:]+[\-\w\:]+\d(?!\s*\d*?\s+AFFIDAVIT|\d*\-|\d*\s*AGREED|\-?\d*\s*RESPONDENT|\d*?\s*\,\s|\d*?\.\s)|\sv\.(?!\s*Douglas|\,|\s*In\s*|\s*As\s|\s*[\w]+\sdid|\s*Mohamed|\s*A formal|\s*Of|\s*Broker|\s*Based|\s*Af.er|\s*Gooch|\s*Stag|\s*Dissatistied|\s*Respondent|\s*Right|\s*At\sthe|\s*\-fhe|\s*Included|\s*All\sof|\s*on|\s*Among|\s*Upon|\s*The)|vs(?:\.|\,)\s*(?!NO|RESP|\s*CASE|\s*[\w\.\,\-]+\s*Respondent|COMP|\,))\s*(.+?)(?:(?<!NO\.\d{2}\-\d{4}\s)RESPONDENT|MS\s39047|(?:A\(\;Rh\sIll\)|AGRIED|AGRFEIT|AGBT\]ED|AGzulED|AGREEI\)|AGREED|ACREED)\s*(?:\(\)l\{l\)llR|ORI\)ER|ORpnR|ORDER|ORDEII)|\.\\\(\;REII\)\sORI\)t\,\)l|(?<!AGREED\s)ORDER(?!\s*to)|DEAR)/
                    while (nameMacher.find()) {
                        name = nameMacher.group(1)
                        temNameList.add(name)
                    }
                } else if (pdfData =~ /(?ism)(?:\s*v\.(?!\s*The|\s*All\sof|\s*Included|\s*On)|r6.\\lm|16.ym|ffiyP\?t|Pfi|F\'ax|MQ.ym|r\]n\&wPfi|\.\sFax|syrPr|rLtNrn|rEb\)|YPP.|Qtym|ritx|Frl|\&.uH1|PP\'|farMrlqr\\\\PPt|Faxt.Yes|\{rtb\)Pr\!|PP\!|UQW\}\?l|F\&\\\)rrsJDPr\,r|\\\!\\\s\.\!\s\\f|t\.1i|l&YP\?r|19t3ri|NrttyPPr|l\!rt\)5\)Pn|i\\rrt.yrr|r\-695J\srar|ilrlELsPf\,l|F\&\\tiffim|\'i\sri\-r|[A-Z]{3,}\s*\d{1,2}\,\s*\d{3,4}(?!\s*\d*\,|\d*\s*c\)\s*OTTICIAL|\s*\d*\s*\(\b\w\b\s|\d*\s*OFFICIAL LETTER OF REPRI\}IAND)|ppr(?!\s*December|\s*February)\s)(.+?)\s*(?:Dear|D\sn\sMr\.|\w\)ca\s*Mr\.|D\,ear|I\)ear|(?:s|B)\-\d{3,6}|s\d{4,6}|Dcar|MadisorLMS|Greetings\:)\s*/) {
                    def nameMacher1 = pdfData =~ /(?ism)(?:\s*v\.(?!\s*The|\s*All\sof|\s*Included|\s*On)|r6.\\lm|16.ym|ffiyP\?t|Pfi|F\'ax|MQ.ym|r\]n\&wPfi|\.\sFax|syrPr|rLtNrn|rEb\)|YPP.|Qtym|ritx|Frl|\&.uH1|PP\'|farMrlqr\\\\PPt|Faxt.Yes|\{rtb\)Pr\!|PP\!|UQW\}\?l|F\&\\\)rrsJDPr\,r|\\\!\\\s\.\!\s\\f|t\.1i|l&YP\?r|19t3ri|NrttyPPr|l\!rt\)5\)Pn|i\\rrt.yrr|r\-695J\srar|ilrlELsPf\,l|F\&\\tiffim|\'i\sri\-r|[A-Z]{3,}\s*\d{1,2}\,\s*\d{3,4}(?!\s*\d*\,|\d*\s*c\)\s*OTTICIAL|\s*\d*\s*\(\b\w\b\s|\d*\s*OFFICIAL LETTER OF REPRI\}IAND)|ppr(?!\s*December|\s*February)\s)(.+?)\s*(?:Dear|D\sn\sMr\.|\w\)ca\s*Mr\.|D\,ear|I\)ear|(?:s|B)\-\d{3,6}|s\d{4,6}|Dcar|MadisorLMS|Greetings\:)\s*/
                    while (nameMacher1.find()) {
                        name = nameMacher1.group(1)
                        temNameList.add(name)
                    }
                }
                if (pdfLink == "http://www.mrec.ms.gov/docs/BROWNKaylaJ.pdf") {
                    temNameList = []
                    temNameList.add('Kayla J. Brown')
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/CHRISTMASCharlesB.pdf") {
                    temNameList = []
                    temNameList.add("Charles B. Christmas")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/COLVINMatthewG.pdf") {
                    temNameList = []
                    temNameList.add("Matthew G. Colvin")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/COPELANDCharlotteA.pdf") {
                    temNameList = []
                    temNameList.add("CHARLOTTE A. COPELAND ; KARLA MARTIN ; JOANNE ROPER")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/CRUMBLYNikiaC.pdf") {
                    temNameList = []
                    temNameList.add("Nikia C. Crumbly")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/GONZALEZMichaelD.pdf") {
                    temNameList = []
                    temNameList.add("Michael D. Gonzalez")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/HINESJRThomasR.pdf") {
                    temNameList = []
                    temNameList.add("Thomas R. Hines, Jr.")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/HIXONMichaelS.pdf") {
                    temNameList = []
                    temNameList.add("Michael S. Hixon")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/HUDSONJRBenG.pdf") {
                    temNameList = []
                    temNameList.add("Ben G.Hudson, Jr.")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/LANESandyLee.pdf") {
                    temNameList = []
                    temNameList.add("Sandly Lee Lane")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/MCCOOLCynthiaL.pdf") {
                    temNameList = []
                    temNameList.add("Cynthia  L. McCool")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/PIPPINJudyE.pdf") {
                    temNameList = []
                    temNameList.add("JUDY E. PIPPIN ; J. WOODY SPIERS")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/STEWARTJRJoeJoey.pdf") {
                    temNameList = []
                    temNameList.add("Joe(Joey) Stewart, Jr.")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/WACHOBJames.pdf") {
                    temNameList = []
                    temNameList.add("James Wachob")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/WIMBERLYMichaelLetterOfReprimand.pdf") {
                    temNameList = []
                    temNameList.add("MICHAEL WIMBERLY")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/DISTEFANOCHARLOTTE.pdf") {
                    temNameList = []
                    temNameList.add("TASHIA D. MCGINN, PRINCIPAL BROKER and CHARLOTTE DISTEFANO, SALESPERSON")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/DANIELSJIMT.pdf") {
                    temNameList = []
                    temNameList.add("JIM T. DANIELS")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/BEHRINGERGRAHAM.pdf") {
                    temNameList = []
                    temNameList.add("GRAHAM BEHRINGER ")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/MESECKEMartinLetterOfReprimand.pdf") {
                    temNameList = []
                    temNameList.add("George Duke Loden")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/HAYNESCora.pdf" | pdfLink == "http://www.mrec.ms.gov/docs/HARTELLRonald.pdf") {
                    temNameList = []
                    temNameList.add("CORA (CORIE) HAYNES ; RONALD (RON) HARTELL")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/GOODWINPaulA.pdf") {
                    temNameList = []
                    temNameList.add("Paul A. Goodwin")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/STANFORDJamesJimmy.pdf") {
                    temNameList = []
                    temNameList.add("James (Jimmy) Stanford")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/SPIERSJWoody.pdf") {
                    temNameList = []
                    temNameList.add('JUDY E. PIPPIN ; J. WOODY SPIERS')
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/SMITHTomLetterOfReprimand.pdf") {
                    temNameList = []
                    temNameList.add("TOM SMITH")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/PHILLIPSTaylorD.pdf") {
                    temNameList = []
                    temNameList.add("Taylor D. Phillips")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/ROBINSONJamesM.pdf") {
                    temNameList = []
                    temNameList.add("James M. Robinson")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/LONDONGeorgeLetterOfReprimand.pdf") {
                    temNameList = []
                    temNameList.add("GEORGE DUKE LODEN")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/MARTINKarla.pdf") {
                    temNameList = []
                    temNameList.add("CHARLOTTE A. COPELAND ; KARLA MARTIN ; JOANNE ROPER")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/SMITHVANIZLARRY.pdf") {
                    temNameList = []
                    temNameList.add("W. LARRY SMITH-VANIZ")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/COCHRANRachelM.pdf") {
                    temNameList = []
                    temNameList.add("Rachel M. Cochran")
                } else if (pdfLink == "http://www.mrec.ms.gov/docs/GEOTISBrittanyS.pdf") {
                    temNameList = []
                    temNameList.add("Brittany S. Geotes")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/BAILEYROSAMOND.pdf" || pdfLink == "http://www.mrec.ms.gov/documents/BRANNONGENTRYHUNTER.pdf") {
                    temNameList = []
                    temNameList.add("ROSAMOND BAILEY")
                    temNameList.add("GENTRY HUNTER BRANNON")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/BOYETTEGLEE_000.pdf" || pdfLink == "http://www.mrec.ms.gov/documents/JENKINSASHLEIGH.pdf") {
                    temNameList = []
                    temNameList.add("L. Thrash")
                    temNameList.add("S. Polles")
                    temNameList.add("A. Jenkins")
                    temNameList.add("L. Boyette")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/CARTERJRPAULNEBO.pdf") {
                    temNameList = []
                    temNameList.add("Paul Carter")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/METCALFMARK.pdf" || pdfLink =="http://www.mrec.ms.gov/documents/PERRYJOSHUA.pdf") {
                    temNameList = []
                    temNameList.add("Mark Metcalf")
                    temNameList.add("Joshua Perry")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/SEALEMIRIAMMORRIS2.pdf") {
                    temNameList = []
                    temNameList.add("WENDY W. CHANCELLOR")
                    temNameList.add("MIRIAM MORRIS SEALE")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/MCBRIDEJERRYLTROFREPRIMAND.pdf") {
                    temNameList = []
                    temNameList.add("Jerry McBride")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/METCALFMARKLtrofReprimand.pdf") {
                    temNameList = []
                    temNameList.add("Mark Metcalf")
                    temNameList.add("EXP Realty")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/BRIDGFORTHBARRYLTROFREPRIMAND.pdf") {
                    temNameList = []
                    temNameList.add("Barry Bridgforth")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/ALLEGUEALLTROFREPRIMAND.pdf") {
                    temNameList = []
                    temNameList.add("Al Allegue")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/ATHERTONJUDYLtrofReprimand.pdf") {
                    temNameList = []
                    temNameList.add("Judy Atherton")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/BARNESSHELLYELOR.pdf") {
                    temNameList = []
                    temNameList.add("Shellye Barnes")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/CABLESABRALTROFREPRIMAND.pdf") {
                    temNameList = []
                    temNameList.add("Sabra Cable")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/CARPENTERPHILIPltrofReprimand.pdf") {
                    temNameList = []
                    temNameList.add("Phillip Carpenter")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/CHONGMIRIAMLTROFREPRIMAND.pdf") {
                    temNameList = []
                    temNameList.add("Miriam Chong")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/GARDNERCALEBLOR.pdf") {
                    temNameList = []
                    temNameList.add("Caleb Gardner")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/METCALFMARKLTROFREPRIMAND2.pdf") {
                    temNameList = []
                    temNameList.add("Mark Metcalf")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/NULLSTEPHANIELOR.pdf") {
                    temNameList = []
                    temNameList.add("Stephanie Null")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/PIGFORDROBERTLOR.pdf") {
                    temNameList = []
                    temNameList.add("Robert Pigford")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/RICHARDLINDSEYLOR.pdf") {
                    temNameList = []
                    temNameList.add("Lindsey Richard")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/SMITHTRELTROFREPRIMAND.pdf") {
                    temNameList = []
                    temNameList.add("Tre Smith")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/BLECKWELLVICKI.pdf") {
                    temNameList = []
                    temNameList.add("Vicki Blackwell")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/BUNCHJONATHAN.pdf") {
                    temNameList = []
                    temNameList.add("Jonathan Bunch")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/CHAMBLISSROSEMARY.pdf") {
                    temNameList = []
                    temNameList.add("Rosemary Chambliss")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/CURRIEBRENDA_000.pdf") {
                    temNameList = []
                    temNameList.add("Brenda Curie")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/HOPPERPAUL.pdf") {
                    temNameList = []
                    temNameList.add("Paul Hopper")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/KETCHINGSWALTER.pdf") {
                    temNameList = []
                    temNameList.add("Walter Ketchings")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/LATIMERJANICE_000.pdf") {
                    temNameList = []
                    temNameList.add("Janice Latimer")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/MARTINSUZANNE.pdf") {
                    temNameList = []
                    temNameList.add("Suzanne Martin")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/MEDLEYJULIE_000.pdf") {
                    temNameList = []
                    temNameList.add("Julie Medley")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/MYERSMARLYS.pdf") {
                    temNameList = []
                    temNameList.add("Marlys Myers")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/NICHOLASSHELIA_000.pdf") {
                    temNameList = []
                    temNameList.add("Shelia Nicholas")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/WARRENMARYKATHERINEKATIE.pdf") {
                    temNameList = []
                    temNameList.add("Mary Katherine Warren")
                } else if (pdfLink == "http://www.mrec.ms.gov/documents/WRIGHTTINAD_000.pdf") {
                    temNameList = []
                    temNameList.add("Tina Wright")
                }



                def nameList = []
                temNameList.each {
                    def temp = []
                    it = sanitizeName(it)
                    temp = it.toString().split(/(?ism)(?:\;|BROI\(ER|non\-resident BROKER|BROKER ASSOCIATE|NON.RESIDENT COMPANY|MANAGINGBROKER|Resident Principal Broker|PRINCIPAL BR\.OKER|Broker|BROK\-ER|PRINCIPAL BROKER|SALESPERSON)/)
                    temp.each { tempIt ->
                        tempIt = tempIt.toString().replaceAll(/\,\s*$/, "").trim()
                        nameList.add(tempIt)
                    }
                }
                def aliasList = []
                nameList.each {
                    it = it.toString().replaceAll(/^\,/, "").trim()
                    it = it.toString().replaceAll(/(?i)^AND\s/, "").replaceAll(/^\&/, "").replaceAll(/^\,/, "").trim()
                    (it, aliasList) = aliasChecker(it)

                    createEntity(it, aliasList, pdfLink, year)
                }
            }
        }

        htmlFormData = htmlFormData.toString().replaceAll(/(?ism)\"\>\d{4}\s*MREC DISCIPLINARY ACTIONS.+?<\/table>/, "")
        def year
        def pdfUrl
        def pdfUrlMacher = htmlFormData =~ /(?ism)\>\s*\<a\s*href=\"(?=docs)(.+?\.pdf)\"\>(201\d)/
        while (pdfUrlMacher.find()) {
            pdfUrl = pdfUrlMacher.group(1)
            year = pdfUrlMacher.group(2)
            pdfUrl = "http://www.mrec.ms.gov/" + pdfUrl
            def pdfData = pdfToTextConverter(pdfUrl)
            def name
            def nameMacher = pdfData =~ /(?ism)(?<!Corporation\.\s)VS(?!\.\,)(.+?)(?:RE\,SPONDENT|(?<!NO\.\d{3}\-\d{4}\s)RESPONDENT(?!\sGuerieri|\')|AGREED\sORDER)/
            def nameMacher1 = pdfData =~ /(?ism)(?<!vs\.\s|cAsE\s)NO\.\s?\d{3}\-\d{4}(?!vs|\sRESPONDENTS|\sPHILIP\sLA\]|\sKEVIN\sw\'|\sLASHAUNDRA|\sERIC\sJ\.|\sBETTY)(.+?)ORDER/
            while (nameMacher.find()) {
                def nameList = []
                name = nameMacher.group(1)
                name = sanitizeName1(name)
                nameList = name.toString().split(/(?ism)(?:\;|BROI\(ER|non\-resident BROKER|BROKER ASSOCIATE|NON.RESIDENT COMPANY|Resident Principal Broker|PRINCIPAL BROKER|Broker|BROK\-ER|SALESPERSON)/)
                def aliasList = []
                nameList.each {
                    it = it.toString().replaceAll(/^\,/, "").trim()
                    it = it.toString().replaceAll(/(?i)^AND\s/, "").replaceAll(/^\&/, "").replaceAll(/^\./, "").replaceAll(/^\,/, "").trim()
                    (it, aliasList) = aliasChecker(it)
                    createEntity(it, aliasList, pdfUrl, year)
                }
            }
            while (nameMacher1.find()) {
                def nameList = []
                name = nameMacher1.group(1)
                name = sanitizeName1(name)
                nameList = name.toString().split(/(?ism)(?:\;|BROI\(ER|non\-resident BROKER|BROKER ASSOCIATE|NON.RESIDENT COMPANY|Resident Principal Broker|PRINCIPAL BROKER|Broker|BROK\-ER|SALESPERSON)/)
                def aliasList = []
                nameList.each {
                    it = it.toString().replaceAll(/^\,/, "").trim()
                    it = it.toString().replaceAll(/(?i)^AND\s/, "").replaceAll(/^\&/, "").replaceAll(/^\./, "").replaceAll(/^\,/, "").trim()
                    (it, aliasList) = aliasChecker(it)
                    createEntity(it, aliasList, pdfUrl, year)
                }
            }


            if (pdfUrl != "http://www.mrec.ms.gov/docs/2018MRECDISCIPLINARYACTIONS.pdf") {
                def nameMacher2 = pdfData =~ /(?ism)(?:List\sof\sBrokers.+?Agreement\:|[\w]+\s*\d{1,2}\,\s*\d{4}(?!\s*\,|\s*\bat\b|\s*for|\s*\bindicated\b|\s*based|\s*where|\s*which|\s*through|\s*\bin\b|\s*when|\s*\bto\b|\s*the\sprospective|\s*and|\;|\.|\s*The\sRankin|\)|\s*decision\.|\s*See))(.+?)(?:[\w]+\sCounty|Kentucky|Harrison|Tennessee|Louisiana|Florida|Alabama|Memphis|South|California|Pearl|(?<!Agreement\:)\s{4})/
                while (nameMacher2.find()) {
                    def nameList = []
                    name = nameMacher2.group(1)
                    name = sanitizeName1(name)
                    nameList = name.toString().split(/(?ism)(?:\;|BROI\(ER|non\-resident\s*BROKER|BROKER ASSOCIATE|NON.RESIDENT COMPANY|Resident Principal Broker|Broker|PRINCIPAL BROKER|SALESPERSON)/)
                    def aliasList = []
                    nameList.each {
                        it = it.toString().replaceAll(/^\,/, "").trim()
                        it = it.toString().replaceAll(/(?i)^AND\s/, "").replaceAll(/^\&/, "").replaceAll(/^\./, "").replaceAll(/^\,/, "").trim()
                        (it, aliasList) = aliasChecker(it)
                        createEntity(it, aliasList, pdfUrl, year)
                    }
                }
                pdfData = pdfData.toString().replaceAll(/(?ism)\A.+?P\sa\sg\se\s*\|\s*192/, "")
                pdfData = pdfData.toString().replaceAll(/(?ism)\A.+?The\sCommission\srevoked\sthe\slicenses\sof\sBroker\sLucas/, "")
                def nameMacher3 = pdfData =~ /(?ism)\s{6}(?!Salesperson|Each|\s*three\s\(3\)|\s*Principal\sBroker)(.+?)(?:\n[\w]+\sCounty|Tennessee|Pearl|Michigan|Alabama|Louisiana)/
                while (nameMacher3.find()) {
                    def nameList = []
                    name = nameMacher3.group(1)
                    name = sanitizeName1(name)
                    nameList = name.toString().split(/(?ism)(?:\;|BROI\(ER|non\-resident\s*BROKER|BROKER ASSOCIATE|NON.RESIDENT COMPANY|Resident Principal Broker|Broker|PRINCIPAL BROKER|SALESPERSON)/)
                    def aliasList = []
                    nameList.each {
                        it = it.toString().replaceAll(/^\,/, "").trim()
                        it = it.toString().replaceAll(/(?i)^AND\s/, "").replaceAll(/^\&/, "").replaceAll(/^\./, "").replaceAll(/^\,/, "").trim()
                        (it, aliasList) = aliasChecker(it)
                        createEntity(it, aliasList, pdfUrl, year)
                    }
                }
            }
        }
    }

    def cleanNames(def name) {
        name = name.replaceAll(/\s+/, " ").trim()

        def clean = [
            "NO\\..*\\d{2}",
            "COMPLAINAN'I'.*",
            "9 Plaza Drive.*",
            "^\\/\$",
            "Sa lcs pcrso n",
            "Broke::.*",
            "case\\s*Greeting.*",
            "April 1-1",
            "\\.\\s*VII\\..*",
            "R\\.ESPONDENT\\s*THIS MATTER comes.*",
            "Shields complained that",
            "\\. Therein",
            "Records obtained during the Commission.*",
            "\\/SALESPE RSON",
            "^et al\$",
            "IN RE: TH.*?MATTER OF",
            "NOTICE OF ALLEGE,D VIOLAT ONSANDOPPORTUNITY.*",
            "NOTICE OF ALLEGED VIOLAT.*",
            "WAIVER OF FORMAL COMPLAINT.*",
            "Leslie Johnson's.*",
            "\\\\o. 06\\{-1909 ANN.*",
            "RE: informal.*",
            "RESPO\\\\DENTS.*",
            "RESPONDBNTS ACITtiED.*",
            "the Commission was advised that.*",
            "Greetings.*",
            "RESPONDBNTS ACITtiED.*",
            "1, il",
            "When contacted by the Commission.*",
            "Cfeefings.*",
            "IN Rf,:.*?MATTER OF",
            "case # 014-2A02.*",
            "OFFICIAL LETTER OF.*",
            "RESPONDF\\],NTS",
            "May further stated that she.*",
            "^i\$",
            "llSLamarBlvd. Suite.*",
            "At closing, Freeman reviewed.*",
            "5E47 Getwell Rd.*",
            "AGREED ORDER.*",
            "\\) and was operating.*",
            "^II\$",
            "\\. The Healing Place Church.*",
            "had begun\\) and that.*",
            "Donald and Barbara Bridges purchased.*",
            "Smith complained, in essence.*",
            "form wherein Hoda was designated.*",
            "David Bourdette who had replaced.*",
            "Terrell complained she.*",
            "99 Timber St.*",
            "\\. Managing",
            "Further investigation revealed that.*"
        ]

        clean.each {
            name = name.replaceAll(/(?sm)$it/, "")
        }

        // REPLACEMENTS
        def tokens = [
            "CIIRIS'TA": "CHRISTA",
            "TAN\\.IA": "TANJA",
            "\\.IEAN": "JEAN",
            "TAM\\}IY": "TAMMY",
            "BI\\]TSY": "BETSY"
        ]

        for (String key: tokens.keySet()) {
            String value = tokens.get(key)
            name = name.replaceAll(/$key/, value);
        }

        name = name.replaceAll(/\s+/, " ").trim()
        name = name.replaceAll(/,$/, "")
        name = name.replaceAll(/\s+/, " ").trim()


        return name
    }

    def sanitizeName(def name) {
        name = name.toString().trim()
        name = name.toString().replaceAll(/(?i)NO\.\s*\:?[A-Z]?\d{1,3}(?:\-|\,)?\d{3,5}/, "")
            .replaceAll(/(?ism).+?Y5yPP./, "")
            .replaceAll(/(?ism).*?OF REPRINI.T.D/, "")
            .replaceAll(/(?ism)(?:RNSPO}IDENT|RESPONDENTS|RXSPONDENTS|RESPONDE\,NTS|Prineipal|RI\.\]SPONDE\,NTS|R\.ESPONDEI\{TS|COMPLAINANT)/, "").trim()
            .replaceAll(/(?ism).+?\-\s(?:F\'?ax|Faxt.Yes|ritx|REPRINI\{T\\D)/, "").trim()
            .replaceAll(/(?ism).*?\w{3,}\s\d{1,2}\,\s*\d{4}/, "")
            .replaceAll(/(?ism)(?<!\-|\,|\d{0,2}\,|\d{0,2}\.\s{1,3})\s\d{4}.+/, "")
            .replaceAll(/(?ism)cAsE\s\#?\s*\d{2,3}\-\d{3,5}/, "")
            .replaceAll(/(?ism)(?:c\/o|p\s+O\s).+/, "").trim()
            .replaceAll(/(?i)(?:resident BROKER|BROKER ASSOCIATE|Broker\.?|PRINCIPAI\, BROKER|PRINCIPAL BROKER|SALESPERSON|BROKER ASSOCIATE RESPONDE\,NTS|BROKER)$/, "").trim()
            .replaceAll(/(?ism)\b(?<!\,|NO\.\s|\d{1,2}\.\s\d|\-\d?|\d{0,2}\,\s|\,\d)\d{3,5}\b\s.+/, "")
            .replaceAll(/(?ism).+?(?:ELsPf\,l|yPPr|Y5yPP\!|t\.1i)/, "")
            .replaceAll(/NO\.\s082\-t9l\sI/, "")
            .replaceAll(/(?ism)I\s\d{2,3}.+/, "")
            .replaceAll(/\(ledric/, "Cedric")
            .replaceAll(/R\-/, "R.")
            .replaceAll(/(?ism).+?(?:wPfi|yPPr|OF REPRINIANI\)|OF REPRI\}IANI\)|III\.\-PRINI\.\\NT\))/, "")
            .replaceAll(/(?ism)COMES NOW.+/, "")
            .replaceAll(/(?ism).+?OFFICIAL\s*(?:I\,F\,TTER\sO\}\'|[\w\,\.\}\-\']+\sOF)\s*(?:RIiPRIIIIAND|REPRINIAND)/, "")
            .replaceAll(/(?ism)MREC\sv\./, "")
            .replaceAll(/(?ism).+?\s\.\s\'\.\-/, "")
            .replaceAll(/(?ism).*?MISSNSIPPI\sREAL\sESTATE\sCOMMISSION/, "")
            .replaceAll(/(?ism)I\sl\d.*/, "")
            .replaceAll(/(?ism)\(INACTIVE\)/, "")
            .replaceAll(/(?ism)I\-\./, "L.")
            .replaceAll(/(?ism)7\sShavon.+/, "")
            .replaceAll(/(?ism)PI\]PE\,/, "PEPE")
            .replaceAll(/(?ism)vs\.?/, "")
            .replaceAll(/\,$/, "")
            .replaceAll(/(?ism)202JLPrestageRoad.+/, "") trim()
            .replaceAll(/\,\d+/, "")
            .replaceAll(/^\,/, "")
            .replaceAll(/(?ism)MA\'I\"I\'HEWS\'/, "MATTHEWS")
            .replaceAll(/(?ism)\.\s\.\s\"/, "")
            .replaceAll(/(?ism).+?RF\,PRIMAND/, "")
            .replaceAll(/(?ism)\#\s\d{3}-[A-Z]?\d{3,4}/, "")
            .replaceAll(/(?ism)DEBOR\{H|I\)EI\}ORAH/, "DEBORAH").trim()
            .replaceAll(/(?i)^AND\s/, "")
            .replaceAll(/^\&/, "")
            .replaceAll(/(?ism)\\\\ade/, "Wade")
            .replaceAll(/(?ism)\.IUSTIN ALI\-EN/, "JUSTIN ALLEN")
            .replaceAll(/\d$/, "")
            .replaceAll(/(?ism)\s[A-Z]\d{3}.*/, "")
            .replaceAll(/(?ism)I\s5\s13.*/, "")
            .replaceAll(/(?ism)HOME INSPECTOR\s*$/, "")
            .replaceAll(/(?ism)(?:4 I lighland|lll Oak|l6l8|715l\{w).*/, "")
            .replaceAll(/(?ism)L'NTREKIN/, "ENTREKIN")
            .replaceAll(/(?ism)Gt\-rulle y\s*I/, "Gourley")
            .replaceAll(/I lazcl/, "Hazel")
            .replaceAll(/(?ism)30 A\s.+/, "")
            .replaceAll(/(?ism)\sITI\,/, " III,")
            .replaceAll(/(?ism)ITTNRY\s/, "HENRY ")
            .replaceAll(/(?ism)\sI"oe/, " Loe")
            .replaceAll(/(?ism)lvl\.\sCore/, "M. Gore")
            .replaceAll(/(?ism)Jua\.n/, "Juan")
            .replaceAll(/(?ism)WIIITEAKER|WHITEAKE\&/, "WHITEAKER")
            .replaceAll(/(?ism)\sITI/, " III")
            .replaceAll(/N\{REC v\./, "")
            .replaceAll(/Sleely/, "Steely")
            .replaceAll(/Perrl\'/, "Perry")
            .replaceAll(/(?ism)NO\.\d{2}[A-Z]\-\d{4}/, "")
            .replaceAll(/PHILIP\.I\./, "PHILIP J.")
            .replaceAll(/WARRINE\&/, "WARRINER,")
            .replaceAll(/Robcrt/, "Robert")
            .replaceAll(/ll cR/, "")
            .replaceAll(/\,IONES/, " JONES")
            .replaceAll(/l\'\,S/, "")
            .replaceAll(/(?ism)^l\s/, "")
            .replaceAll(/(?)GItEEN/,"GREEN")
            .replaceAll(/(?i)Partcn/,"Parten")
            .replaceAll(/(?i)DEII.ISE/,"DENISE")
            .replaceAll(/(?ism)RT\]ESE/, "REESE").trim()
        return name
    }

    def sanitizeName1(def name) {
        name = name.toString().replaceAll(/(?ism)cAsE\sNO\.\s\d{1,2}\-\d{4}/, "").trim()
            .replaceAll(/(?ism)LLC\,\s*Company/, "LLC, BROKER")
            .replaceAll(/(?i)Inactive Licensee/, "BROKER")
            .replaceAll(/(?i)Incorporated Brokerage Firm/, "BROKER")
            .replaceAll(/(?i)Branch Office\, Starkville/, "BROKER")
            .replaceAll(/(?i)Non\-Resident Salesperson/, "BROKER")
            .replaceAll(/(?ism)\,\s*Company/, " BROKER").trim()
            .replaceAll(/(?i)Non\-Resident Principal Broker/, "BROKER")
            .replaceAll(/(?i)Broker\/Salesperson/, "BROKER")
            .replaceAll(/(?i)Non\-Resident Broker/, "BROKER")
            .replaceAll(/(?i)Branch Office in Bay S\. Louis/, "BROKER")
            .replaceAll(/(?i)Branch Office/, "BROKER")
            .replaceAll(/^\s*\,/, "")
            .replaceAll(/(?ism)Rev\.\s\d{1,2}\/\d{4}\sP\sa\sg\se\s*.\s*\d{1,3}/, "").trim()
            .replaceAll(/(?ism)NO\.\d{2,3}\-\d{4}\s*RESPONDENTS/, "")
            .replaceAll(/(?ism)COMPLAINANT|R..SPONDENTS|RESPONIDENT|RESPONDENTS/, "").trim()
            .replaceAll(/(?ism)RESPONDENT/, "")
            .replaceAll(/(?ism)(?:NON\-RESIDENT\sBROK\-?ER|Non\-Resident\sCompany|Inactive)\s*$/, "").trim()
            .replaceAll(/(?ism)NON.RESIDENT COMPANY$/, "")
            .replaceAll(/(?ism)Non\-Resident\sPrincipal\sBroker/, "")
            .replaceAll(/(?ism)PRINCIPAL\s*BROKER/, "BROKER")
            .replaceAll(/(?i)BROKER$/, "")
            .replaceAll(/(?ism)\,\s*Resident/, "")
            .replaceAll(/(?i)Salesperson$/, "")
            .replaceAll(/(?ism).*?(?<!JR.,\s{1,5})NO\.\d{2,3}\-(?:\d{4}|[A-Z]\d{3}|\d{3}[A-Z])/, "")
            .replaceAll(/(?ism).+?NO\.[\w]{2,3}-\d{4}/, "")
            .replaceAll(/(?ism).*?\w{3,}\s\d{1,2}\,\s*\d{4}/, "").trim()
            .replaceAll(/(?i)Brokerage Firm$/, "")
            .replaceAll(/(?ism)^and\s/, "")
            .replaceAll(/(?ism)Ai\[D\s/, "")
            .replaceAll(/(?ism)KIINNETII/, "KENNETH")
            .replaceAll(/..IEST\./, "WEST")
            .replaceAll(/(?ism)BROIGR\;$/, "").trim()
            .replaceAll(/(?ism)^\s*\./, "")
            .replaceAll(/.TRNY./, "\"TREY\"")
            .replaceAll(/(?i)w\'/, "W.")
            .replaceAll(/(?ism)LA\]..DERS/, "LANDERS")
            .replaceAll(/(?ism)RICHARI\}/, "RICHARD")
            .replaceAll(/(?ism)TTIOMPSON/, "THOMPSON")
            .replaceAll(/(?ism)\(Managing\)/, "")
            .replaceAll(/(?ism)Coldwell Banker\,/, "Coldwell Banker ")
            .replaceAll(/(?i)\, Paula Ricks/, " Paula Ricks")
            .replaceAll(/(?ism)Inactive/, "")
            .replaceAll(/(?ism)J\. Richard\, Grant/,"Richard J. Grant")
            .replaceAll(/(?i)\s.TREY.\s/," (TREY) ")
            .replaceAll(/(?i)JR\./, "").trim()
            .replaceAll(/(?:\,|\/|\:)$/, "").trim()

        return name
    }

    def aliasChecker(def name) {
        def alias
        def aliasList = []
        name = name.toString().replaceAll(/\“/,"(").replaceAll(/\”/,")")
        name = name.toString().replaceAll(/(?i)(?:\ba[\.\/]?k[\.\/]?a\b|\bf[\.\/]?k[\.\/]?a\b|\b(?:t[\.\/]?)?d[\.\/]?b[\.\/]?a\b|\bn[\.\/]?k[\.\/]?a\b)/, "a.k.a")
        if (name =~ /(?i)^.*?\((.+)\)/) {
            def aliasMatcher = name =~ /(?i)^.*?\((.+)\)/
            while (aliasMatcher.find()) {
                alias = aliasMatcher.group(1)
                def remove = aliasMatcher.group(1)
                name = name.toString().replaceAll(/\($remove\)/, "").replaceAll(/(?i)\(\)/, "").trim()

                aliasList.add(alias)
            }
        }
        if (name =~ /(?i)^.*?\"(.+)\"/) {
            def aliasMatcher = name =~ /(?i)^.*?\"(.+)\"/
            while (aliasMatcher.find()) {
                alias = aliasMatcher.group(1)
                def remove = aliasMatcher.group(1)
                name = name.toString().replaceAll(/$remove/, "").replaceAll(/(?i)\"\"/, "").trim()

                aliasList.add(alias)
            }
        }
        if (name =~ /(?i)a\.k\.a(.+?)/) {

            def aliasMatcher = name =~ /(?i)a\.k\.a(.+)/
            while (aliasMatcher.find()) {
                alias = aliasMatcher.group(1)
                name = name.toString().replaceAll(/(?i)a\.k\.a(.+)/, "").trim()
                if (alias =~ /(?i)a\.k\.a(.+)/) {
                    alias = alias.split(/(?i)a\.k\.a/)
                    alias.each { it ->
                        aliasList.add(it)
                    }
                } else {
                    aliasList.add(alias)
                }
            }
        }

        if (name =~ /(?i)(.+)a\.k\.a/) {

            def aliasMatcher = name =~ /(?i)(.+)a\.k\.a/
            while (aliasMatcher.find()) {
                alias = aliasMatcher.group(1)

                name = name.toString().replaceAll(/(?i)(.+)a\.k\.a/, "").trim()
                if (alias =~ /(?i)(.+)a\.k\.a/) {
                    alias = alias.split(/(?i)a\.k\.a/)
                    alias.each { it ->

                        aliasList.add(it)
                    }
                } else {
                    aliasList.add(alias)
                }
            }
        }
        return [name, aliasList]
    }

    def createEntity(def name, def aliasList, def url, def year) {
        name = name.toString().trim()
        name = name.toString().replaceAll(/(?ism)Jr\.$/, "").replaceAll(/(?ism)Sr\.$/, "").replaceAll(/\.$/, "").replaceAll(/\,$/, "").replaceAll(/(?ism)Jr$/, "").trim()
        name = name.toString().replaceAll(/\,$/, "").trim()

        name = cleanNames(name)

        def entity = null
        if (!name.toString().isEmpty()) {
            def entityType = detectEntity(name)
            entity = context.findEntity(["name": name, "type": entityType]);
            if (!entity) {
                entity = context.getSession().newEntity()
                entity.setName(name)
                entity.setType(entityType)
            }
            aliasList.each {
                if (it) {
                    it = it.toString().replaceAll(/(?ism)Jr\.?\s*$/, "").replaceAll(/\.\s*$/, "").replaceAll(/\,$/, "")
                    it = it.toString().replaceAll(/(?s)\s+/, " ").trim()
                    entity.addAlias(cleanNames(it))
                }
            }
            //adding address
            ScrapeAddress scrapeAddress = new ScrapeAddress()
            scrapeAddress.setProvince("Mississippi")
            scrapeAddress.setCountry("United States")
            entity.addAddress(scrapeAddress)

            if (url) {
                entity.addUrl(url)
            }
            ScrapeEvent event = new ScrapeEvent()
            def status = "This entity appears on the Mississippi Real Estate Commission list of Disciplinary Actions."
            event.setDescription(status)
            def actionDate = "01/01/" + year
            actionDate.toString().trim()

            if (actionDate) {
                actionDate = context.parseDate(new StringSource(actionDate), ["dd/MM/yyyy"] as String[])
                event.setDate(actionDate)
            }
            entity.addEvent(event)
        }
    }

    def detectEntity(def name) {
        def type
        if (name =~ /^\S+$/) {
            type = "O"
        } else {
            type = entityType.detectEntityType(name)
            if (type.equals("P")) {
                if (name =~ /(?i)(?:Aid|Estate|\bCoastway\b|Americas|Realty|FS\sCARD|BANKER|Alternative\sL\.|Creativity|Pharmacy|\bVets\b|\bObesity\b|\bHomeless\b|Sanctuary|Kayla|Change|Purpose|Life|Welfare|Term|Dynamics|Abuse|Cub|Rebound|Speak|Police|Boys|Charities|Opportunity|Pennsylvania|Vision|Mission|Alternatives|Adoptions|Camp|Embassy|Christie|Geek|Fall|Policy|Publications|Cure|Brotherhood|Studios|Forum|Powerkids|Workshop|Hightower|Families|Citizens|Wishes|Nationalist|Brothers|Cancer|Autism|Hope|Americans|Clinic|Medicine|Animals|Future)/) {
                    type = "O"
                }
            }
            if (type.equals("O")) {
                if (name =~ /(?i)(?:stephen\sL.|JAMES WEST MOORE|JULIA FIELD|KENNETH R. WEST|WENDY W. CHANCELLOR|Germain Thompson|Marisol Carmen Gonzalez|Vickey P. Ward|JEAN WEST|Carl\sWilliam\sMerck|\bTONI\b|\bKayla\b|Debra\sM\.\sHarper\-West|HOPE\sCOOK|\bPAUL\b|\bSTEPHANIE\b|Chi\sM\.\sHo|Scott|\bSANDRA\b|CHRISTIE|Kimberly|Thomas\sShields)|Jason\sHunt/) {
                    type = "P"
                }
            }
        }
        return type
    }

    def pdfToTextConverter(def pdfUrl) {
        try {
            List<String> pages = ocrReader.getText(pdfUrl)
            return pages
        } catch (NoClassDefFoundError e){
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
        } catch(Exception e){
            return "PDF Error"
        }
    }

    def invokeGET(url, paramsMap = [:], headersMap = [:], cache = false, tidy = false, miscData = [:]) {
        try {
            Map dataMap = [url: url, type: "GET", params: paramsMap, tidy: tidy, headers: headersMap, cache: cache]
            dataMap.putAll(miscData)
            return context.invoke(dataMap)
        } catch (Exception e) {
            println(e.getMessage())
        }
    }

    def invokeBinary(url, type = null, paramsMap = null, cache = false, clean = true, miscData = [:]) {
        //Default type is GET
        Map dataMap = [url: url, type: type, params: paramsMap, clean: clean, cache: cache]
        dataMap.putAll(miscData)
        return context.invokeBinary(dataMap)
    }


}