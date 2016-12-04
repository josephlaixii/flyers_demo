package com.flyers.db_software_incorporateion.db_flyers.api;

import com.flyers.db_software_incorporateion.db_flyers.ListofWebsites;

import java.util.List;

import retrofit2.Callback;
import retrofit2.http.GET;

/**
 * Created by House on 12/2/2016.
 */

public interface FlyerAPI {

    @GET("/listofwebsites.json")
    public void getlistofWebsites(Callback<List<ListofWebsites>> response);

}
