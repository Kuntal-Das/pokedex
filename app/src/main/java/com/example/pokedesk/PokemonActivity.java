package com.example.pokedesk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView numberTextView;
    private TextView type0TextView;
    private TextView type1TextView;
    private String url;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_activity);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        url = getIntent().getStringExtra("url");
        nameTextView = findViewById(R.id.pokemon_name);
        numberTextView = findViewById(R.id.pokemon_number);
        type0TextView = findViewById(R.id.pokemon_type0);
        type1TextView = findViewById(R.id.pokemon_type1);

        LoadPokemon();
    }

    private void LoadPokemon() {
        nameTextView.setText("");
        type0TextView.setText("");
        type1TextView.setText("");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            var name = response.getString("name");
                            nameTextView.setText(name.substring(0, 1).toUpperCase() + name.substring(1, name.length() - 2));
                            numberTextView.setText(String.format("#%03d", response.getInt("id")));

                            JSONArray typeEntries = response.getJSONArray("types");
                            for (int i = 0; i < typeEntries.length(); i++) {
                                JSONObject typeEntry = typeEntries.getJSONObject(i);
                                var slot = typeEntry.getInt("slot");
                                var typeName = typeEntry.getJSONObject("type").getString("name");
                                if (slot == 1) {
                                    type0TextView.setText(typeName);
                                } else if (slot == 2) {
                                    type0TextView.setText(typeName);
                                }
                            }
//                            notifyDataSetChanged();
                        } catch (JSONException ex) {
                            Log.e("cs50", "Pokemon Details JSON error ", ex);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Pokemon Details list error", error);
            }
        });
        requestQueue.add(request);
    }
}