package com.checkout.checkoutmobile;

import java.util.Locale;

public interface Config {
    String PRODUCTION_SERVER_URL = "http://checkout-env.eba-icztdryu.ca-central-1.elasticbeanstalk.com";
    int DEFAULT_TIMEOUT = 3000;
    Locale FORMAT_LOCALE = Locale.CANADA;
}
