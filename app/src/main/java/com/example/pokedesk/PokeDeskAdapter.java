package com.example.pokedesk;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PokeDeskAdapter extends RecyclerView.Adapter<PokeDeskAdapter.PokeDeskViewHolder> {

    private List<Pokemon> pokemons = new ArrayList<>();
    private RequestQueue requestQueue;

    private void LoadPokemon(){
        String url = "https://pokeapi.co/api/v2/pokemon";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++){
                                JSONObject result = results.getJSONObject(i);
                                String name = result.getString("name");
                                pokemons.add(new Pokemon(i,
                                        name.substring(0,1).toUpperCase()+name.substring(1,name.length()-2),
                                        result.getString("url")));
                            }
                            notifyDataSetChanged();
                        } catch (JSONException ex) {
                            Log.e("cs50", "JSON error ", ex);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Pokemon list error", error);
            }
        });
        requestQueue.add(request);
    }

    @NonNull
    @Override
    public PokeDeskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pokedesk_row,parent,false);

        return new PokeDeskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokeDeskViewHolder holder, int position) {
        Pokemon currPokeMon = pokemons.get(position);
        holder.textView.setText(currPokeMon.getName());
        holder.containerView.setTag(currPokeMon);
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    PokeDeskAdapter(Context context)
    {
        requestQueue = Volley.newRequestQueue(context);
        LoadPokemon();
    }

    public static class PokeDeskViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout containerView;
        public TextView textView;
        PokeDeskViewHolder(View view){
            super(view);
            containerView = view.findViewById(R.id.pokedesk_row);
            textView = view.findViewById((R.id.pokedesk_row_text_view));

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pokemon currPokemon = (Pokemon) containerView.getTag();
                    Intent intent = new Intent(v.getContext(), PokemonActivity.class);
                    intent.putExtra("name",  currPokemon.getName());
                    intent.putExtra("number",  currPokemon.getNumber());

                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
