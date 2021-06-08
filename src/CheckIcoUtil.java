import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class CheckIcoUtil {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String PROXY = "Nastavene-url-pro-proxy";
    private static final String PORT = "Nastaveny-port-pro-proxy";

    public boolean checkIcoUtil(String ico) {
        try {
            String url = "http://wwwinfo.mfcr.cz/cgi-bin/ares/xar.cgi";
            URL obj = new URL(url);


            Properties systemProperties = System.getProperties();
            systemProperties.setProperty("http.proxyHost",PROXY);
            systemProperties.setProperty("http.proxyPort",PORT);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","text/xml; charset=UTF-8");
            con.setRequestProperty("SOAPAction", "");

            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            String formatDateTime = localDateTime.format(formatter);

            String requestorEmail = "vas.skutecny@sbcz.cz";

            String xml = "<are:Ares_dotazy " +
                    "xmlns:are=\"http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_request_rzp/v_1.0.4\" " +
                    "xmlns:dtt=\"http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.4\" " +
                    "xmlns:udt=\"http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.1\" " +
                    "xmlns:dc=\"http://purl.org/dc/elements/1.0/\" " +
                    "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" " +
                    "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                    "xsi:schemaLocation=\"http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_request_rzp/v_1.0.4 http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_request_rzp/v_1.0.4/ares_request_rzp_v_1.0.4.xsd\" " +
                    "vystup_format=\"XML\" " +
                    "Id=\"Ares_dotaz\" " +
                    "user_mail=\""+requestorEmail+"\" " +
                    "dotaz_datum_cas=\""+formatDateTime+"\" " +
                    "validation_XSLT=\"http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_request_orrg/v_1.0.0/ares_request_orrg.xsl\" " +
                    "answerNamespaceRequired=\"http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_answer_rzp/v_1.0.5\" " +
                    "dotaz_typ=\"Vypis_RZP\" " +
                    "dotaz_pocet=\"2\">\n" +
                    "<are:Dotaz>\n" +
                    "<are:Pomocne_ID>1</are:Pomocne_ID>\n" +
                    "<are:ICO>"+ico+"</are:ICO>\n" +
                    "</are:Dotaz>\n" +
                    "</are:Ares_dotazy>";
            con.setDoOutput(true);

            System.out.println("\n#===---< XML >---===#\n"+xml+"\n#===---< konec XML >---===#\n");
            con.setConnectTimeout(5000); //5 sec in milliseconds
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(xml);
            wr.flush();
            wr.close();
            String responseStatus = con.getResponseMessage();
            System.out.println(responseStatus);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                                        con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("response:");
            System.out.println(response);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            //return false;
        }
        return true;
    }

    public static void main(String[] args) {
        CheckIcoUtil iCoUtil = new CheckIcoUtil();
        iCoUtil.checkIcoUtil("SEM-SI-ZADEJ-ICO");
    }
}
