package be.vkt.PayPerMail;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

/**
 *  This is the model class for E-Mail payments
 */
public class MailPayment {

    private final StringProperty amount;
    private final StringProperty name;
    private final StringProperty email;

    private final StringProperty url;

    /**
     * Constructor with minimal initial data
     *
     * @param amount The amount (in EUR) to be paid.
     * @param email  The E-mail address of the payer.
     */
    public MailPayment(String amount, String email) {
        this.amount = new SimpleStringProperty(amount);
        this.email = new SimpleStringProperty(email);
        this.name = new SimpleStringProperty("");
        this.url = new SimpleStringProperty("");
    }

    /**
     * Constructor with initial data
     *
     * @param amount The amount (in EUR) to be paid.
     * @param name   The name of the payer. This name will be used as "Card Holder" / CN on the PayPage.
     * @param email  The E-mail address of the payer.
     */
    public MailPayment(String amount, String name, String email) {
        this.amount = new SimpleStringProperty(amount);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.url = new SimpleStringProperty("");
    }

    /**
     * Default constructor
     */
    public MailPayment() {
        this("0.0", "Niemand@VlaamseKampeertoeristen.be");
    }

    public String getAmount() {
        return amount.get();
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getUrl() {
        return url.get();
    }

    private void setUrl(String url) {
        this.url.set(url);
    }

    private String calculateSha1(String inputString) {
        System.out.println("Hashing the follwoing string:");
        System.out.println(inputString);
        String sha1 = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(inputString.getBytes("utf8"));
            sha1 = String.format("%040X", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("Results in:");
        System.out.println(sha1);
        return sha1;
    }

    //Helper function to URL-encode parameter values.
    private String encodeVal(String val) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(val, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            encoded = val;
        }
        return encoded;
    }

    public void calculateUrl() {
        final String shaPassPhrase = "VlaamseKampeertoeristen2017";
        final String basePage = "https://secure.paypage.be/ncol/test/orderstandard.asp?";

        String stringToHash = ""; /* According to
        https://support-paypage.v-psp.com/nl/nl/guides/integration%20guides/e-commerce/security-pre-payment-check#shainsignature
        The string to be hashed must contain only non-empty fields and they must be in alphabetical order.
        Some fields come straight from the form, others are calculated and some have fixed values.
        There is no separator between the fields in the stringToHash, but in the same location the shaPassPhrase must be
        inserted.
        For these reasons, the string is built up piece by piece, but the order of the blocks may not be altered!
        */
        String parameterString = "";

        //AMOUNT
        String a = getAmount();
        System.out.println("entered AMOUNT: raw a = "+a);
        a = a.replace(',', '.'); //in case somebody used a comma instead of a point
        System.out.println("replaced comma by point => a = "+a);
        a = a.replaceAll("[^\\d.]", "");  //Now get rid of anything but "0123456789."
        System.out.println("regexp => a = "+a);
        System.out.println("a.length() = "+a.length());
        setAmount(a);
        if (a != null && a.length() > 0) {
            //try to parse the amount as a Float
            Float amountFl;
            try {
                amountFl = Float.parseFloat(a);
            } catch (NumberFormatException e) {
                System.out.println("Even after cleanup, the amount could not be parsed: "+a);
                e.printStackTrace();
                amountFl = -99.99f; //just to let the rest of the program run
            }

            //the amount is coded as an integer: the float value multiplied by 100 (a number of eurocents, if you want)
            Float amount100f = 100.00f * amountFl;
            int amount100i = amount100f.intValue();
            stringToHash += "AMOUNT="+Integer.toString(amount100i)+shaPassPhrase;
            parameterString += "AMOUNT="+Integer.toString(amount100i)+"&";
            System.out.println("added amount to stringToHash: "+stringToHash);
            System.out.println("added amount to parameterString: "+parameterString);
        } else {
            System.out.println("amount was empty (after cleanup)!");
            //bail out: a payment without a decent amount is useless
            setUrl(""); return;
        }

        //CN (Name)
        String n = getName();
        if (n != null && n.length() > 0) {
            stringToHash += "CN="+n+shaPassPhrase;
            parameterString += "CN="+encodeVal(n)+"&";
            System.out.println("added name to stringToHash: "+stringToHash);
            System.out.println("added name to parameterString: "+parameterString);
        } else {
            System.out.println("name was empty => no change to stringToHash: "+stringToHash);
            System.out.println("name was empty => no change to parameterString: "+parameterString);
        }

        //CURRENCY
        stringToHash += "CURRENCY=EUR"+shaPassPhrase;
        parameterString += "CURRENCY=EUR&";

        //EMAIL
        String e = getEmail();
        if (e != null && e.length() > 0) {
            stringToHash += "EMAIL="+e+shaPassPhrase;
            parameterString += "EMAIL="+encodeVal(e)+"&";
            System.out.println("added email to stringToHash: "+stringToHash);
            System.out.println("added email to parameterString: "+parameterString);
        } else {
            System.out.println("email was empty => no change to stringToHash: "+stringToHash);
            System.out.println("email was empty => no change to parameterString: "+parameterString);
        }

        //LANGUAGE
        stringToHash += "LANGUAGE=nl_BE"+shaPassPhrase;
        parameterString += "LANGUAGE=nl_BE&";
        System.out.println("added language to stringToHash: "+stringToHash);
        System.out.println("added language to parameterString: "+parameterString);

        //ORDERID
        String payLocation = "Mobicar";  //toDo: Turn this into a setting of some sort
        String timestamp = Long.toString(Instant.now().toEpochMilli());
        stringToHash += "ORDERID="+payLocation+"_"+timestamp+shaPassPhrase;
        parameterString += "ORDERID="+encodeVal(payLocation+"_"+timestamp)+"&";
        System.out.println("added orderid to stringToHash: "+stringToHash);
        System.out.println("added orderid to parameterString: "+parameterString);

        //PSPID
        String pspId = "kampeertest";  //toDo: Turn this into a setting of some sort
        stringToHash += "PSPID="+pspId+shaPassPhrase;
        parameterString += "PSPID="+encodeVal(pspId)+"&";
        System.out.println("added pspid to stringToHash: "+stringToHash);
        System.out.println("added pspid to parameterString: "+parameterString);

        //TITLE
        stringToHash += "TITLE=Betaling aan Vlaamse Kampeertoeristen VZW op "+payLocation+shaPassPhrase;
        parameterString += "TITLE="+encodeVal("Betaling aan Vlaamse Kampeertoeristen VZW op "+payLocation)+"&";
        System.out.println("added title to stringToHash: "+stringToHash);
        System.out.println("added title to parameterString: "+parameterString);


        //SHASIGN
        String shaSign = calculateSha1(stringToHash);
        parameterString += "SHASIGN="+shaSign; //last parameter => no more & needed.
        System.out.println("added shasign to parameterString: "+parameterString);

        StringBuilder sb = new StringBuilder();
        sb.append(basePage).append(parameterString);
        setUrl(sb.toString());
        System.out.println("The generated URL is: "+getUrl());
    }
}
