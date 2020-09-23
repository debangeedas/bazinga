package com.example.covidtest;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.covidtest.adapters.RssFeedAdapter;
import com.example.covidtest.data.RssFeedModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private RecyclerView mRecyclerView;
    private ArrayList<RssFeedModel> mFeedModelList = new ArrayList<>();
    public RssFeedAdapter rssFeedAdapter;
    private ProgressBar pb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView covidTestButton = view.findViewById(R.id.covid_test_button);
        covidTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CovidInstruction1.class));
            }
        });

        ImageView recordEcgButton = view.findViewById(R.id.record_ecg_button);
        recordEcgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EcgInstruction1.class));
            }
        });

        ImageView diabetesTestButton = view.findViewById(R.id.diabetes_test_button);
        diabetesTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DiabetesInstruction1.class));
            }
        });

        ImageView refreshButton = view.findViewById(R.id.refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchFeedTask().execute((Void) null);
            }
        });

        pb = view.findViewById(R.id.progressBar);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        new FetchFeedTask().execute((Void) null);
        return view;
    }

    public ArrayList<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String description = null;
        String link = null;
        ArrayList<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(getInputStream(new URL("https://medicalxpress.com/rss-feed/")), "UTF_8");

            boolean insideItem = false;

            // Returns the type of current event: START_TAG, END_TAG, START_DOCUMENT, END_DOCUMENT etc..
            int eventType = xpp.getEventType(); //loop control variable

            while (eventType != XmlPullParser.END_DOCUMENT) {
                //if we are at a START_TAG (opening tag)
                if (eventType == XmlPullParser.START_TAG) {
                    //if the tag is called "item"
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                    }
                    //if the tag is called "title"
                    else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (insideItem) {
                            // extract the text between <title> and </title>
                            title = xpp.nextText();
                        }
                    }
                    //if the tag is called "description"
                    else if (xpp.getName().equalsIgnoreCase("description")) {
                        if (insideItem) {
                            // extract the text between <link> and </link>
                            description = xpp.nextText();
                        }
                    }
                    //if the tag is called "link"
                    else if (xpp.getName().equalsIgnoreCase("link")) {
                        if (insideItem) {
                            // extract the text between <link> and </link>
                            link = xpp.nextText();
                            items.add(new RssFeedModel(title, description, link));
                        }
                    }
                }
                //if we are at an END_TAG and the END_TAG is called "item"
                else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    insideItem = false;
                }

                eventType = xpp.next(); //move to next element
            }
            return items;
        } finally {
            inputStream.close();
        }
    }

    public InputStream getInputStream(URL url) {
        try {
            //openConnection() returns instance that represents a connection to the remote object referred to by the URL
            //getInputStream() returns a stream that reads from the open connection
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> implements RssFeedAdapter.OnFeedListener {

        private String urlLink = "https://medicalxpress.com/rss-feed/";

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = parseFeed(inputStream);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            pb.setVisibility(View.INVISIBLE);
            if (success) {
                rssFeedAdapter = new RssFeedAdapter(getContext(), mFeedModelList, this);
                rssFeedAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(rssFeedAdapter);
            } else {
                Toast.makeText(getActivity(), "Enter a valid Rss feed url", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFeedClick(int position) {
            RssFeedModel r = mFeedModelList.get(position);
            Uri uri = Uri.parse(r.getLink());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
