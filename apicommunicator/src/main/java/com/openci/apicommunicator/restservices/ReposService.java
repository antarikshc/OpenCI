package com.openci.apicommunicator.restservices;

import androidx.annotation.Nullable;

import com.openci.apicommunicator.R;
import com.openci.apicommunicator.callbacks.IAPICallBack;
import com.openci.apicommunicator.interfaces.IRepos;
import com.openci.apicommunicator.models.LibApp;
import com.openci.apicommunicator.models.ReposResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.openci.apicommunicator.restservices.APIClient.getPrivateClient;
import static com.openci.apicommunicator.restservices.APIClient.getPublicClient;

/**
 * Created by Vicky on 10-01-2018.
 */

public class ReposService {

    public static void getRepos(String public_travis_token, String private_travis_token, @Nullable final IAPICallBack callback){
        String public_authorization_token;
        String private_authorization_token;
        String include = "repository.last_started_build";
        Retrofit retrofit;
        Call<ReposResponse> reposResponseCall;

        if(public_travis_token != null){
            public_authorization_token = "token " + public_travis_token;
            retrofit = getPublicClient();
            IRepos repos = retrofit.create(IRepos.class);
            reposResponseCall = repos.getRepos(
                    public_authorization_token,
                    include
            );
        }
        else{
            private_authorization_token = "token " + private_travis_token;
            retrofit = getPrivateClient();
            IRepos repos = retrofit.create(IRepos.class);
            reposResponseCall = repos.getRepos(
                    private_authorization_token,
                    include
            );
        }
        // Yet to deal if both tokens are null
        reposResponseCall.enqueue(new Callback<ReposResponse>() {
            @Override
            public void onResponse(Call<ReposResponse> call, Response<ReposResponse> response) {
                if(response != null){
                    callback.onSuccess(response.body());
                }
                else{
                    callback.onError(LibApp.getContext().getString(R.string.null_general_response));
                }
            }

            @Override
            public void onFailure(Call<ReposResponse> call, Throwable t) {
                if(t != null && t.getMessage() != null){
                    callback.onError(t.getMessage());
                }
                else{
                    callback.onError(LibApp.getContext().getString(R.string.null_failure_response));
                }
            }
        });
    }
}
