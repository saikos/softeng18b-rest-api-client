package gr.ntua.ece.softeng18b.client.model

import groovy.transform.Canonical

@Canonical class PriceInfoList extends Paging {

    List<PriceInfo> prices

}
