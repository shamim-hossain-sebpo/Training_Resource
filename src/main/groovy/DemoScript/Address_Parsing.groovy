package shamim.io


import com.rdc.importer.scrapian.util.ModuleLoader
import com.rdc.scrape.ScrapeAddress

def moduleLoaderVersion = "###############################"
final def moduleFactory = ModuleLoader.getFactory(moduleLoaderVersion)

def entity = context.getSession().newEntity();
entity.setName("TESTER");
entity.setType("P");
def given = "1305 Rue Pise, Brossard, QC J4W 2P7, Canada; and 203-760 Rue Galt, Montreal, QC H4G 2P7, Canada; and 6271 Rue Beaulieu, Montreal, QC, H4E 3E9, Canada"
def street_sanitizer = { street ->
    fixStreet(street)
}
println(street_sanitizer)
def addressParser = moduleFactory.getGenericAddressParser(context)
given.split("; and").each { ad ->
    ad = sanitizeAddress(ad)
    def addrMap = addressParser.parseAddress([text: ad, force_country: true])
    ScrapeAddress scrapeAddress = addressParser.buildAddress(addrMap, [street_sanitizer: street_sanitizer])
    if (scrapeAddress) {
        entity.addAddress(scrapeAddress)
    }
}

def fixStreet(def address) {
    return address.replaceAll(/(?s)\s+/, " ").trim()
}

String sanitizeAddress(String rawFormat) {
    //format Address to : lane address,postal code, city, province, country
    return rawFormat.replaceAll(/(?ism)\s+/, " ")
}



