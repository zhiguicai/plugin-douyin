package com.hb.douyinplugin;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UploadContactsService {

    @FormUrlEncoded
    @POST("/aweme/v1/upload/contacts/")
    Call<String> uploadContacts(@Query(value = "need_unregistered_user") String str, @FieldMap Map<String, String> map);

}
