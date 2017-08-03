package info.reportissues.communitycommunicator.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import info.reportissues.communitycommunicator.CommCommVolley;
import info.reportissues.communitycommunicator.Constants;
import info.reportissues.communitycommunicator.R;
import info.reportissues.communitycommunicator.models.User;

/**
 * Created by howardpassmore on 8/2/17.
 */

public class RegisterDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.registration)
                .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_registration,null))
                .setPositiveButton(R.string.register, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText passwordField = (EditText) getDialog().findViewById(R.id.registerDialogPassword);
                        String password = passwordField.getText().toString();
                        EditText confirmField = (EditText) getDialog().findViewById(R.id.registerDialogConfirm);
                        String confirm = confirmField.getText().toString();
                        if(password.equals(confirm)) {
                            EditText emailField = (EditText) getDialog().findViewById(R.id.registerDialogEmail);
                            String email = emailField.getText().toString();

                            User toCreate = new User(email,password);
                            registerUser(toCreate);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    private void registerUser(final User user) {
        final Gson gson = new Gson();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SERVER_URL + "/user",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity().getApplicationContext(),"YAY",Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",user.getEmail());
                params.put("password",user.getPassword());
                return params;
            }
        };

        CommCommVolley.getInstance(null).addToRequestQueue(stringRequest);
    }
}