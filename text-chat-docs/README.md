OpenTok Android Text Chat Component
===================================

The OpenTok Android Text Chat Component provides code for adding a user interface component
for displaying and capturing text chat messages in an Andriod application.

This component is currently available as a beta version. To obtain a binary version of
the component, contact[text-chat-beta@tokbox.com](mailto:text-chat-beta@tokbox.com).

The text-chat-sample directory in this repo uses the Text Chat Component and the
[OpenTok signaling  API](https://tokbox.com/developer/guides/signaling/android/) to provide
text chat in an OpenTok session.

## Using the Text Chat Component

The com.opentok.android.ui.textchat.widget package includes the classes and interfaces
that define the OpenTok Android Text Chat Component API.

The TextChatFragment class defines an Android Fragment for adding and controling the text chat
user interface:

* It includes user interface elements for entering a chat message to send and displaying sent
  and received messages.

* It includes a TextChatListener interface and an `onMessageReadyToSend(msg)` method
  you can implent to receive events when the user clicks the Send button (to send a message).

* It includes an `addMessage(msg)` method you call to add new received messages
  to the message list. The ChatMessage class defines the message.

The following sections provide details.

### Instantiating and configuring the text chat fragment

Instantiate a TextChatFragment and add it to a container:

```java
mTextChatFragment = new TextChatFragment();
int containerId = R.id.fragment_textchat_container;
mFragmentTransaction = getFragmentManager().beginTransaction();
mFragmentTransaction.add(containerId, mTextChatFragment, "TextChatFragment").commit();
```

You can (optionally) set the maximum length of an outgoing text message, by calling the
`TextChatFragment.setMaxTextLength(length)` method (passing in the maximum length for outgoing
messages):

```java
mTextChatFragment.setMaxTextLength(1050);
```
By default the maximum length is 1000 characters. 

You can (optionally) set the sender ID and sender alias and the sender name for messages created
by the client (outgoing messages):

```java
mTextChatFragment.setSenderInfo("1234", "Me");
```

This method takes two parameters:

* `senderId` (String) - The local client's sender ID. The TextChatFragment compares the sender ID of
  a message you add to the message list (see "Displaying received messages") to the sender ID you
  use in the `TextChatFragment.setSenderInfo()` method. Based on that it determines if the message
  is sent by the local client or by someone else. Note, however, that outgoing messages composed
  when the user clicks the Send button are automatically added to the message list (as sent
  messages).

* `senderAlias` (String) - The local client's sender alias, which is the name to display for
  outgoing messages the user creates when clicking the Send button.

### Receiving events when the user clicks the Send button

To receive events when the user clicks the Send button, you can do either of
the following:

* Call the `TextChatFragment.setTextChatListener(listener)` method:

   ```java
   mTextChatFragment.setTextChatListener(this);
   ```
   
   Then implement the `TextChatFragment.TextChatListener.onMessageReadyToSend(msg)`
   
   ```java
   @Override
   public boolean onMessageReadyToSend(ChatMessage msg) {
       Log.d(LOGTAG, "TextChat listener: onMessageReadyToSend: " + msg.getText());
   }
   ```

* Create a subsclass of the TextChatFragment class and override the
  `TextChatFragment.TextChatListener.onMessageReadyToSend(msg)` method:

   ```java
   mTextChatFragment.setTextChatListener(this);
   ```

   Then override the `TextChatFragment.TextChatListener.onMessageReadyToSend(msg)`
   method:

   ```java
   @Override
   public boolean onMessageReadyToSend(ChatMessage msg) {
       Log.d(LOGTAG, "TextChat listener: onMessageReadyToSend: " + msg.getText());
   }
   ```

In the `onMessageReadyToSend(msg)` method that you override, you can
call the `getText()` method of the `msg` parameter to get the text of the
new message. Then you can implement code to process the outgoing message. For example,
the sample app in this repo uses the OpenTok signaling API to broadcast the chat
message to clients connected to an OpenTok session.

Sent messages are automatically displayed in the message list.

### Displaying received messages

When your app receives a text message, use the `TextChatFragment.addMessage(msg)` method
to add it to the message list user interface.

First create the ChatMessage object:

```java
msg = new ChatMessage("5678", "Bob", "Hello.");
mTextChatFragment.addMessage(msg);
```
The `ChatMessage()` constructor has three parameters:

* `senderId` (String) - The sender ID that identifies the sender of the message. If this
  matches the sender ID for the local client, the TextChatFragment object display's the
  message as a sent message in the message list. Otherwise, it displays it as a received
  message. (See the discussion of the `TextChatFragment.setSenderInfo()` method in
  "Instantiating and configuring the text chat fragment.")

* `senderAlias` (String) - The name of the sender of the message to display in the message list.

* `text` (String) - The text of the message to display in the message list.

In the example, these values are hardcoded. However, in a real app, you would pass
in values for the specific message.

Calling the `TextChatFragment.addMessage(msg)` method causes the message to be displayed in
the TextChatFragment's message list.

By default, a chat message is assigned the current time on the client. However, you can set it
to another time by calling the `setTimestamp(long time)` method of the ChatMessage object.
