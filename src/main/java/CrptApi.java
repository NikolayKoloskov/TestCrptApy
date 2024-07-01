import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import org.json.simple.JSONObject;

public class CrptApi {
    private final ReentrantLock lock = new ReentrantLock();
    private final TimeUnit timeUnit;
    private final int requestLimit;
    private long lastRequestTime = 0;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
    }

    public void createDocument(Object document, String signature) throws InterruptedException, IOException {
        lock.lock();
        try {
            long currentTime = timeUnit.toNanos(System.currentTimeMillis());
            long timeSinceLastRequest = currentTime - lastRequestTime;
            if (timeSinceLastRequest < timeUnit.toNanos(requestLimit)) {
                long sleepTime = timeUnit.toNanos(requestLimit) - timeSinceLastRequest;
                TimeUnit.NANOSECONDS.sleep(sleepTime);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("description", new JSONObject().put("participantInn", "string"));
            jsonObject.put("doc_id", "string");
            jsonObject.put("doc_status", "string");
            jsonObject.put("doc_type", "LP_INTRODUCE_GOODS");
            jsonObject.put("importRequest", true);
            jsonObject.put("owner_inn", "string");
            jsonObject.put("participant_inn", "string");
            jsonObject.put("producer_inn", "string");
            jsonObject.put("production_date", "2020-01-23");
            jsonObject.put("production_type", "string");
            jsonObject.put("products", new JSONObject().put("certificate_document", "string"));
            jsonObject.put("products", new JSONObject().put("certificate_document_date", "2020-01-23"));
            jsonObject.put("products", new JSONObject().put("certificate_document_number", "string"));
            jsonObject.put("products", new JSONObject().put("owner_inn", "string"));
            jsonObject.put("products", new JSONObject().put("producer_inn", "string"));
            jsonObject.put("products", new JSONObject().put("production_date", "2020-01-23"));
            jsonObject.put("products", new JSONObject().put("tnved_code", "string"));
            jsonObject.put("products", new JSONObject().put("uit_code", "string"));
            jsonObject.put("products", new JSONObject().put("uitu_code", "string"));
            jsonObject.put("reg_date", "2020-01-23");
            jsonObject.put("reg_number", "string");
            String jsonDocument = jsonObject.toJSONString();
            String encodedDocument = Base64.getEncoder().encodeToString(jsonDocument.getBytes());
            String encodedSignature = Base64.getEncoder().encodeToString(signature.getBytes());
            URL url = new URL("https://ismp.crpt.ru/api/v3/lk/documents/create");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.getOutputStream().write(encodedDocument.getBytes());
            connection.getOutputStream().write(encodedSignature.getBytes());
            connection.getInputStream();
            lastRequestTime = currentTime;
        } finally {
            lock.unlock();
        }
    }
}

