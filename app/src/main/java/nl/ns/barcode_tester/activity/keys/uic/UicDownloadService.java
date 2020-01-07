package nl.ns.barcode_tester.activity.keys.uic;

import nl.ns.barcode_tester.activity.keys.uic.domain.KeyResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by joelhaasnoot on 16/11/2016.
 */

public interface UicDownloadService {

    @GET("download.php")
    Call<KeyResponse> getKeys();
}
