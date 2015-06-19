package com.opentok.android.ui.textchat.widget;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.opentok.android.ui.textchat.R;
import com.opentok.android.ui.textchat.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TextChatFragment extends Fragment {

    private final static String LOG_TAG = "TextChatFragment";

    private Context mContext;
    Handler mHandler;

    private ArrayList<ChatMessage> mMsgsList = new ArrayList<ChatMessage>();
    private MessageAdapter mMessageAdapter;

    private ListView mListView;
    private Button mSendButton;
    private EditText mMsgEditText;
    private TextView mMsgCharsView;
    private TextView mMsgNotificationView;
    private View mMsgDividerView;

    private int maxTextLength = 1000; // by default the maximum length is 1000.

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.textchat_fragment_layout,
                container, false);

        mListView = (ListView) rootView.findViewById(R.id.msgs_list);
        mSendButton = (Button) rootView.findViewById(R.id.send_button);
        mMsgCharsView = (TextView) rootView.findViewById(R.id.characteres_msg);
        mMsgCharsView.setText(String.valueOf(maxTextLength));
        mMsgNotificationView = (TextView) rootView.findViewById(R.id.new_msg_notification);
        mMsgEditText = (EditText) rootView.findViewById(R.id.edit_msg);
        mMsgDividerView = (View) rootView.findViewById(R.id.divider_notification);
        mMsgEditText.addTextChangedListener(mTextEditorWatcher);

        mSendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mMessageAdapter = new MessageAdapter(getActivity(), R.layout.sent_msg_row_layout, mMsgsList);

        mListView.setAdapter(mMessageAdapter);
        mMsgNotificationView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showNewMsgNotification(false);
                mListView.smoothScrollToPosition(mMessageAdapter.getMessagesList().size() - 1);
                return false;
            }
        });

        return rootView;
    }

    //Callback to indicate a new output message is ready to be sent
    public interface TextChatListener {
        public boolean onMessageReadyToSend(String msgStr);
    }

    protected TextChatListener textChatListener;


    public void setTextChatListener(TextChatListener textChatListener) {
        this.textChatListener = textChatListener;
    }

    //To set the maximum length of the ChatMessage
    public void setMaxTextLength(int length) {
        maxTextLength = length;
    }

    //Add message to the UI text-chat component
    public void addMessage(ChatMessage msg) {
        Log.i(LOG_TAG, "New message " + msg.getText() + " is ready to be added.");
        boolean messageGroup = false;

        if (msg != null) {

            boolean visible = isNewMessageVisible();
            showNewMsgNotification(visible);

            //generate message timestamp
            Date date = new Date();
            if (msg.getTimestamp() == 0) {
                msg.setTimestamp(date.getTime());
            }

            mMessageAdapter.add(msg);
        }
    }

    //To check if the next item is visible or not.
    //Add a notification if it is not visible to allow the scroll the ChatMessage list.
    private void showNewMsgNotification(boolean visible) {
        mMsgNotificationView.setTextColor(getResources().getColor(R.color.text));
        mMsgNotificationView.setText("New messages");

        if (visible) {
            mMsgDividerView.setVisibility(View.VISIBLE);
            mMsgNotificationView.setVisibility(View.VISIBLE);
        } else {
            mMsgNotificationView.setVisibility(View.GONE);
            mMsgDividerView.setVisibility(View.GONE);
        }
    }

    //To check if the new message is visible in the list
    private boolean isNewMessageVisible() {

        int last = mListView.getLastVisiblePosition();
        int transpose = 0;
        View currentBottomView;
        currentBottomView = mListView.getChildAt(last);

        if (mListView.getCount() > 1) {
            while (currentBottomView == null) {
                transpose++;
                currentBottomView = mListView.getChildAt(last - transpose);
            }
            if (last == mListView.getCount() - 1 && currentBottomView.getBottom() <= mListView.getHeight()) {
                mListView.setScrollContainer(false);
                return false;
            } else {
                mListView.setScrollContainer(true);
                return true;
            }
        }
        return false;
    }

    //Click sendButton
    private void sendMessage() {
        //checkMessage
        mMsgEditText.setEnabled(false);
        String msgStr = mMsgEditText.getText().toString();
        if (!msgStr.isEmpty()) {
            boolean msgError = textChatListener.onMessageReadyToSend(msgStr);

            if (msgError) {
                Log.d(LOG_TAG, "Error to send the message");
                mMsgEditText.setEnabled(true);
                mMsgEditText.setFocusable(true);
                mMsgNotificationView.setText("Unable to send message. Retry");
                mMsgNotificationView.setTextColor(Color.RED);
                mMsgNotificationView.setVisibility(View.VISIBLE);

            } else {
                mMsgEditText.setEnabled(true);
                mMsgEditText.setFocusable(true);
                mMsgEditText.setText("");
                mListView.smoothScrollToPosition(mMessageAdapter.getCount());
            }
        }
    }

    protected void onMessageReadyToSend(String msgStr) {
        if (this.textChatListener != null) {
            Log.d(LOG_TAG, "onMessageReadyToSend");
            this.textChatListener.onMessageReadyToSend(msgStr);
        }
    }

    // Countdown the characters left
    private TextWatcher mTextEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int chars_left = maxTextLength - s.length();

            mMsgCharsView.setText(String.valueOf((maxTextLength - s.length())));
            if (chars_left < 10) {
                mMsgCharsView.setTextColor(Color.RED);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
