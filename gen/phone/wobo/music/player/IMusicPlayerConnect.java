/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /root/workspace/sdk_adt_eclipse_in_one_folder/PhoneWoboMusic/src/phone/wobo/music/player/IMusicPlayerConnect.aidl
 */
package phone.wobo.music.player;
public interface IMusicPlayerConnect extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements phone.wobo.music.player.IMusicPlayerConnect
{
private static final java.lang.String DESCRIPTOR = "phone.wobo.music.player.IMusicPlayerConnect";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an phone.wobo.music.player.IMusicPlayerConnect interface,
 * generating a proxy if needed.
 */
public static phone.wobo.music.player.IMusicPlayerConnect asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof phone.wobo.music.player.IMusicPlayerConnect))) {
return ((phone.wobo.music.player.IMusicPlayerConnect)iin);
}
return new phone.wobo.music.player.IMusicPlayerConnect.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_setPlayListAndPlay:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<phone.wobo.music.model.MusicInfo> _arg0;
_arg0 = data.createTypedArrayList(phone.wobo.music.model.MusicInfo.CREATOR);
int _arg1;
_arg1 = data.readInt();
this.setPlayListAndPlay(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_setPlayList:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<phone.wobo.music.model.MusicInfo> _arg0;
_arg0 = data.createTypedArrayList(phone.wobo.music.model.MusicInfo.CREATOR);
this.setPlayList(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getFileList:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<phone.wobo.music.model.MusicInfo> _arg0;
_arg0 = new java.util.ArrayList<phone.wobo.music.model.MusicInfo>();
this.getFileList(_arg0);
reply.writeNoException();
reply.writeTypedList(_arg0);
return true;
}
case TRANSACTION_getCurrentMusicInfo:
{
data.enforceInterface(DESCRIPTOR);
phone.wobo.music.model.MusicInfo _result = this.getCurrentMusicInfo();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_rePlay:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.rePlay();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_play:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
boolean _result = this.play(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_pause:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.pause();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_playNext:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.playNext();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_playPre:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.playPre();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_seekTo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
boolean _result = this.seekTo(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_stop:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.stop();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_exit:
{
data.enforceInterface(DESCRIPTOR);
this.exit();
reply.writeNoException();
return true;
}
case TRANSACTION_destroy:
{
data.enforceInterface(DESCRIPTOR);
this.destroy();
reply.writeNoException();
return true;
}
case TRANSACTION_getCurPosition:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCurPosition();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getDuration:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getDuration();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getPlayState:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getPlayState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getPlayMode:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getPlayMode();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getAudioSessionId:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getAudioSessionId();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCurPlayIndex:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCurPlayIndex();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getMusicFileType:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getMusicFileType();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_startDownLoad:
{
data.enforceInterface(DESCRIPTOR);
phone.wobo.music.model.MusicInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = phone.wobo.music.model.MusicInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.startDownLoad(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setPlayMode:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setPlayMode(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sendPlayStateBrocast:
{
data.enforceInterface(DESCRIPTOR);
this.sendPlayStateBrocast();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements phone.wobo.music.player.IMusicPlayerConnect
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void setPlayListAndPlay(java.util.List<phone.wobo.music.model.MusicInfo> playList, int index) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeTypedList(playList);
_data.writeInt(index);
mRemote.transact(Stub.TRANSACTION_setPlayListAndPlay, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setPlayList(java.util.List<phone.wobo.music.model.MusicInfo> playList) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeTypedList(playList);
mRemote.transact(Stub.TRANSACTION_setPlayList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getFileList(java.util.List<phone.wobo.music.model.MusicInfo> playList) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getFileList, _data, _reply, 0);
_reply.readException();
_reply.readTypedList(playList, phone.wobo.music.model.MusicInfo.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public phone.wobo.music.model.MusicInfo getCurrentMusicInfo() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
phone.wobo.music.model.MusicInfo _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentMusicInfo, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = phone.wobo.music.model.MusicInfo.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean rePlay() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_rePlay, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean play(int position) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(position);
mRemote.transact(Stub.TRANSACTION_play, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean pause() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pause, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean playNext() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_playNext, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean playPre() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_playPre, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean seekTo(int rate) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(rate);
mRemote.transact(Stub.TRANSACTION_seekTo, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean stop() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stop, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void exit() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_exit, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void destroy() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_destroy, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int getCurPosition() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurPosition, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getDuration() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDuration, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getPlayState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPlayState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getPlayMode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPlayMode, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getAudioSessionId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAudioSessionId, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getCurPlayIndex() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurPlayIndex, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getMusicFileType() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMusicFileType, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void startDownLoad(phone.wobo.music.model.MusicInfo mp3) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((mp3!=null)) {
_data.writeInt(1);
mp3.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_startDownLoad, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setPlayMode(int mode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(mode);
mRemote.transact(Stub.TRANSACTION_setPlayMode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sendPlayStateBrocast() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sendPlayStateBrocast, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_setPlayListAndPlay = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setPlayList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getFileList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getCurrentMusicInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_rePlay = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_play = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_pause = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_playNext = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_playPre = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_seekTo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_stop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_exit = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_destroy = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_getCurPosition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_getDuration = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_getPlayState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_getPlayMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_getAudioSessionId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_getCurPlayIndex = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_getMusicFileType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_startDownLoad = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_setPlayMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_sendPlayStateBrocast = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
}
public void setPlayListAndPlay(java.util.List<phone.wobo.music.model.MusicInfo> playList, int index) throws android.os.RemoteException;
public void setPlayList(java.util.List<phone.wobo.music.model.MusicInfo> playList) throws android.os.RemoteException;
public void getFileList(java.util.List<phone.wobo.music.model.MusicInfo> playList) throws android.os.RemoteException;
public phone.wobo.music.model.MusicInfo getCurrentMusicInfo() throws android.os.RemoteException;
public boolean rePlay() throws android.os.RemoteException;
public boolean play(int position) throws android.os.RemoteException;
public boolean pause() throws android.os.RemoteException;
public boolean playNext() throws android.os.RemoteException;
public boolean playPre() throws android.os.RemoteException;
public boolean seekTo(int rate) throws android.os.RemoteException;
public boolean stop() throws android.os.RemoteException;
public void exit() throws android.os.RemoteException;
public void destroy() throws android.os.RemoteException;
public int getCurPosition() throws android.os.RemoteException;
public int getDuration() throws android.os.RemoteException;
public int getPlayState() throws android.os.RemoteException;
public int getPlayMode() throws android.os.RemoteException;
public int getAudioSessionId() throws android.os.RemoteException;
public int getCurPlayIndex() throws android.os.RemoteException;
public int getMusicFileType() throws android.os.RemoteException;
public void startDownLoad(phone.wobo.music.model.MusicInfo mp3) throws android.os.RemoteException;
public void setPlayMode(int mode) throws android.os.RemoteException;
public void sendPlayStateBrocast() throws android.os.RemoteException;
}
