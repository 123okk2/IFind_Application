package com.example.ifind.serverFunction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.example.ifind.compareFunction.compareSearchInfo;
import com.example.ifind.kidFunction.MyKidInfo;
import com.example.ifind.lossChildFunction.LongLossChildInfo;
import com.example.ifind.lossChildFunction.ShortLossChildInfo;
import com.example.ifind.replyFunction.replyInfo;
import com.example.ifind.userInfoFunction.MyComments;
import com.example.ifind.userInfoFunction.MyPost;
import com.example.ifind.userInfoFunction.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ServerConnectionManager {
    //컨텍스트 (다이얼로그 호출용)
    Context context;

    //url
    String baseURL ="secretURL";
    String serverURL;
    String errCode = "";
    HttpURLConnection conn;
    JSONObject jsonObject = null;

    //실행 결과, 에러 이유
    boolean result;
    String errcd;

    //베이스64 인코드 데이터
    String incodedBase64Img;

    //어떤 기능을 실행할지
    int whichFunction = 0;

    //출력용
    private UserInfo ui;
    private ArrayList<MyPost> myposts;
    private ArrayList<MyComments> myComments;
    private ArrayList<String> myKids;
    private MyKidInfo myKidInfo;
    private ArrayList<ShortLossChildInfo> slc = new ArrayList<>();
    private ArrayList<LongLossChildInfo> llc = new ArrayList<>();
    private ShortLossChildInfo sli;
    private  LongLossChildInfo lli;
    private ArrayList<replyInfo> comments = new ArrayList<>();
    private String similarity;
    private ArrayList<compareSearchInfo> compareList;

    //입력용
    String id, name, pw;

    public ServerConnectionManager() {  }

    //회원 정보 관리
    //1. 회원 가입
    public Boolean registerNewUser(String id, String pw, String addr, String name, String phone, Bitmap photo) {
        serverURL=baseURL + "/member/register";
        serverURL += "?id=" + id +"&pw=" +pw +"&addr=" + addr +"&phone=" + phone + "&name=" + name;

        if(photo!=null) {
            incodedBase64Img = encodeToBase64(photo, Bitmap.CompressFormat.JPEG, 50);
            serverURL += "&photo=" + incodedBase64Img;
        }


        whichFunction = 1;

        JSONParse();


        return result;
    }

    //2. 회원 탈퇴
    public Boolean unregister(String id, String pw) {
        serverURL=baseURL+"/member/unregister";
        serverURL += "?id=" + id +"&pw=" +pw;


        whichFunction = 2;

        JSONParse();

        return result;
    }
    //3. 회원 정보 조회
    public UserInfo getMemberInfo(String id, String pw) {
        serverURL=baseURL+"/member/info";
        serverURL += "?id=" + id +"&pw=" +pw;


        whichFunction = 3;
        this.id = id;
        this.pw = pw;
        JSONParse();
        return ui;

    }
    //4. 회원 정보 수정
    public Boolean EditUserInfo(String id, String pw, String newPw, String name, String addr, String phone, Bitmap photo) {
        serverURL=baseURL+"/member/edit";
        serverURL += "?id=" + id +"&addr=" + addr +"&phone=" + phone + "&name=" + name + "&pw=" + pw;

        if(!newPw.equals("")) serverURL += "&new_pw=" + newPw;

        if(photo != null) {
            incodedBase64Img = encodeToBase64(photo, Bitmap.CompressFormat.JPEG, 50);
            serverURL += "&photo=" + incodedBase64Img;
        }

        whichFunction = 4;

        JSONParse();

        return result;
    }

    //로그인
    public Boolean logIn(String id, String pw) {
        serverURL=baseURL+"/member/login";
        serverURL += "?id=" + id +"&pw=" +pw;

        whichFunction = 5;

        JSONParse();

        return result;
    }

    //추가정보 관리
    //1. 내 신고내역 조회
    public ArrayList<MyPost> posts(final String id) {
        serverURL=baseURL+"/my/posts";
        serverURL += "?id=" + id + "&name=" + name;

        this.id = id;
        this.name = name;

        myposts = new ArrayList<>();


        whichFunction = 6;

        JSONParse();

        return myposts;
    }
    //2. 내 제보내역 조회
    public ArrayList<MyComments> comments(final String id) {
        serverURL=baseURL+"/my/comments";
        serverURL += "?id=" + id;

        this.id = id;

        myComments = new ArrayList<>();


        whichFunction = 7;

        JSONParse();


        return myComments;
    }
    //3. 내 아이목록 조회
    public ArrayList<String> myKids(String id) {
        serverURL=baseURL+"/my/children";
        serverURL += "?id=" + id;

        myKids = new ArrayList<>();


        whichFunction = 8;

        JSONParse();


        return myKids;
    }

    //아이목록
    //1, 아이정보 등록
    public Boolean registerMyKid(String id, String name, int age, Bitmap photo, String feature) {
        serverURL=baseURL+"/child/register";
        serverURL += "?id=" + id +"&name=" + name +"&age=" + age +"&feature=" + feature;

        if(photo!=null) {
            incodedBase64Img = encodeToBase64(photo, Bitmap.CompressFormat.JPEG, 50);
            serverURL += "&photo=" + incodedBase64Img;
        }


        whichFunction = 9;

        JSONParse();


        return result;
    }
    //2. 아이정보 수정
    public Boolean EditMyKid(String id, String name, String new_Name, int age, Bitmap photo, String feature) {
        serverURL=baseURL+"/child/edit";
        serverURL += "?id=" + id +"&name=" + name +"&new_name=" + new_Name + "&age=" + age +"&feature=" + feature;

        if(photo!=null) {
            incodedBase64Img = encodeToBase64(photo, Bitmap.CompressFormat.JPEG, 50);
            serverURL += "&photo=" + incodedBase64Img;
        }


        whichFunction = 10;

        JSONParse();


        return result;
    }
    //3. 아이 정보 삭제
    public Boolean deleteMyKid(String id, String name) {
        serverURL=baseURL+"/child/delete";
        serverURL += "?id=" + id +"&name=" + name;


        whichFunction = 11;

        JSONParse();


        return result;
    }
    //4. 아이 정보 상세조회
    public MyKidInfo MyKids(String id, String name) {
        serverURL=baseURL+"/child/info";
        serverURL += "?id=" + id +"&name=" + name;
        myKidInfo = new MyKidInfo("errorOccure",1, null, "1");


        whichFunction = 12;

        JSONParse();


        return myKidInfo;
    }

    //미아 신고
    //1. 미아 신고 등록
    public Boolean postShortLossChild(String id, String name, int type, int age, Bitmap photo, String missingDate, String missingPlace, String feature) {
        serverURL=baseURL + "/post/write";
        serverURL += "?id=" + id +"&name=" + name + "&type=" + type + "&age=" + age +"&feature=" + feature +"&missing_place="+missingPlace+"&missing_date="+missingDate;

        if(photo!=null) {
            incodedBase64Img = encodeToBase64(photo, Bitmap.CompressFormat.JPEG, 50);
            serverURL += "&photo1=" + incodedBase64Img;
        }


        whichFunction = 13;

        JSONParse();


        return result;
    }
    public Boolean postLongLossChild(String id, String name, int type1, int type2, int age, Bitmap oldPhoto, Bitmap youngPhoto, String missingDate, String missingPlace, String feature) {
        serverURL=baseURL + "/post/write";
        serverURL += "?id=" + id +"&name=" + name + "&type=" + type1 + "&type2=" + type2 + "&age=" + age +"&feature=" + feature +"&missing_place="+missingPlace+"&missing_date="+missingDate;

        if(oldPhoto != null) {
            incodedBase64Img = encodeToBase64(oldPhoto, Bitmap.CompressFormat.JPEG, 50);
            serverURL += "&photo1=" + incodedBase64Img;
        }
        if(youngPhoto != null) {
            incodedBase64Img = encodeToBase64(youngPhoto, Bitmap.CompressFormat.JPEG, 50);
            serverURL += "&photo2=" + incodedBase64Img;
        }


        whichFunction = 14;

        JSONParse();

        return result;
    }
    //2. 미아 신고 수정
    public Boolean editLossChild(String id, String name, String newName, int type, int age, Bitmap photo1, String missingDate, String missingPlace, String feature) {
        serverURL=baseURL + "/post/edit";
        serverURL += "?id=" + id +"&name=" + name + "&type=" + type + "&age=" + age +"&feature=" + feature +"&missing_place="+missingPlace+"&missing_date="+missingDate;

        if(photo1 != null) {
            incodedBase64Img = encodeToBase64(photo1, Bitmap.CompressFormat.JPEG, 50);
            serverURL += "&photo1=" + incodedBase64Img;
        }


        whichFunction = 15;

        JSONParse();


        return result;
    }
    public Boolean editLossChild(String id, String name, String newName, int type, int type2, int age, Bitmap oldPhoto, Bitmap youngPhoto, String missingDate, String missingPlace, String feature) {
        serverURL=baseURL + "/post/edit";
        serverURL += "?id=" + id +"&name=" + name + "&type=" + type + "&type2=" + type2 + "&age=" + age +"&feature=" + feature +"&missing_place="+missingPlace+"&missing_date="+missingDate;

        if(oldPhoto != null) {
            incodedBase64Img = encodeToBase64(oldPhoto, Bitmap.CompressFormat.JPEG, 50);
            serverURL += "&photo1=" + incodedBase64Img;
        }
        if(youngPhoto != null) {
            incodedBase64Img = encodeToBase64(youngPhoto, Bitmap.CompressFormat.JPEG, 50);
            serverURL += "&photo2=" + incodedBase64Img;
        }


        whichFunction = 16;

        JSONParse();


        return result;
    }
    //3. 미아 신고 삭제
    public Boolean deleteLossChild(String id, String name) {
        serverURL=baseURL + "/post/delete";
        serverURL += "?id=" + id +"&name=" + name;


        whichFunction = 17;

        JSONParse();


        return result;
    }
    //4. 미아 신고 목록조회
    public ArrayList<ShortLossChildInfo> getLossChildList(String cityName) {
        serverURL=baseURL + "/post/list";
        serverURL += "?city_name=" + cityName;

        slc = new ArrayList<>();


        whichFunction = 18;

        JSONParse();


        return slc;
    }
    public ArrayList<LongLossChildInfo> getLongLossChildList(int type) {
        serverURL=baseURL + "/post/list";
        serverURL += "?type=" + type;

        llc = new ArrayList<>();


        whichFunction = 19;

        JSONParse();


        return llc;
    }
    //5. 미아신고 상세조회
    public ShortLossChildInfo getShortLossChild(final String id, final String name) {
        serverURL=baseURL + "/post/info";
        serverURL += "?pid=" + id + "&name=" + name;

        this.id = id;
        this.name = name;


        whichFunction = 20;
        JSONParse();


        return sli;
    }
    public LongLossChildInfo getLongLossChild(final String id, final String name) {
        serverURL=baseURL + "/post/info";
        serverURL += "?pid=" + id + "&name=" + name;

        this.id = id;
        this.name = name;


        whichFunction = 21;
        JSONParse();


        return lli;
    }
    //미아제보
    //1. 미아제보 등록
    public Boolean writeComment(String id, String pid, String name, String contents, String date, int type) {
        serverURL=baseURL + "/comment/write";
        serverURL += "?id=" + id +"&pid=" + pid +"&name=" + name + "&contents=" + contents + "&date=" + date + "&type=" + type;


        whichFunction = 22;
        JSONParse();


        return result;
    }
    //2. 미아제보 수정
    public Boolean editComment(String id, String pid, String cid, String name, String contents, String date) {
        serverURL=baseURL + "/comment/edit";
        serverURL += "?id=" + id +"&pid=" + pid +"&cid=" + cid +"&name=" + name + "&contents=" + contents + "&date=" + date;


        whichFunction = 23;
        JSONParse();


        return result;
    }
    //3. 미아제보 삭제
    public Boolean deleteComment(String id, String pid, String cid, String name) {
        serverURL=baseURL + "/comment/delete";
        serverURL += "?id=" + id +"&pid=" + pid +"&cid=" + cid +"&name=" + name;


        whichFunction = 24;
        JSONParse();


        return result;
    }
    //4. 미아 제보 전체 조회
    public ArrayList<replyInfo> getComments(String pid, final String name, int type, String id) {
        serverURL=baseURL + "/comment/list";
        serverURL += "?pid=" + pid + "&type=" + type + "&name=" + name + "&id=" + id;

        this.name = name;

        comments = new ArrayList<>();


        whichFunction = 25;
        JSONParse();


        return comments;
    }
    //사진비교
    //1. 사진으로 미아검색
    //특정 미아와 사진 비교
    public String comparePic(Bitmap usrImg, Bitmap kidImg,String pid) {
        serverURL=baseURL + "/compare_face";
        if(usrImg != null) serverURL += "?usrImg=" + encodeToBase64(usrImg, Bitmap.CompressFormat.JPEG, 50);
        if(kidImg != null) serverURL += "&kidImg=" + encodeToBase64(kidImg, Bitmap.CompressFormat.JPEG, 50);
        serverURL += "&pid=" + pid;
        similarity="0";


        whichFunction = 26;
        JSONParse();

        return similarity;
    }

    public ArrayList<compareSearchInfo> searchPic(Bitmap pic, String id) {
        System.out.println("여기까지1");
        serverURL=baseURL+"/search_face";
        if(pic != null) serverURL += "?img=" + encodeToBase64(pic, Bitmap.CompressFormat.JPEG, 50);
        serverURL += "&uid="+id;
        System.out.println("여기까지2");
        System.out.println(id);

        compareList = new ArrayList<>();

        whichFunction = 27;
        JSONParse();

        return compareList;
    }

    //공통 : 에러 이유
    private void setErrCode(String errCode) { this.errCode = errCode; }
    public String getErrCode() { return errCode; }

    //Base64 인,디코더
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.URL_SAFE);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, Base64.URL_SAFE);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    //AsyncTask

    public void JSONParse() {
        try {
        System.out.println(serverURL);
        URL url = new URL(serverURL);
        conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        //
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(15000);

        //
        conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
        int response = conn.getResponseCode();
        if (response == HttpURLConnection.HTTP_OK) {
            StringBuilder builder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject(builder.toString());
            System.out.println(jsonObject);

            try {
                switch (whichFunction) {
                    case 1:
                        //회원가입
                        if (jsonObject.getBoolean("result")) {
                            result = true;
                        } else {
                            result = false;
                            if (jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }

                        if(!result) {
                            if (errcd.equals("EXISTING_ID")) {
                                setErrCode("이미 존재하는 아이디입니다.");
                            } else {
                                setErrCode("회원가입에 실패했습니다.");
                            }
                        }
                        break;
                    case 2:
                        //회원탈퇴
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("MISMATCH")) {
                                setErrCode("아이디와 패스워드가 일치하지 않습니다.");
                            } else {
                                setErrCode("회원탈퇴에 실패했습니다.");
                            }
                        }
                        break;
                    case 3:
                        //회원정보 조회
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                            if(jsonObject.has("contents")) {
                                JSONObject jsonArray = jsonObject.getJSONObject("contents");
                                String addr = jsonArray.getString("addr");
                                String name = jsonArray.getString("name");
                                String phone = jsonArray.getString("phone");
                                Bitmap photo = null;

                                if (jsonArray.has("photo")) {
                                    String photoURL = jsonArray.getString("photo");
                                    System.out.println(photoURL);
                                    photo = decodeBase64(photoURL);
                                }

                                ui = new UserInfo(id, pw, addr, name, phone, photo);
                                //String id, String pw, String addr, String name, String phone, Bitmap photo
                            }

                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                            ui = new UserInfo("errorOccure", "1", "1", "1", null);
                        }
                        if(!result) {
                            if (errcd.equals("MISMATCH")) {
                                setErrCode("아이디와 비밀번호가 일치하지 않습니다.");
                            } else {
                                setErrCode("회원 정보 조회에 실패했습니다.");
                            }
                        }
                        break;
                    case 4:
                        //회원정보 수정
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("MISMATCH")) {
                                setErrCode("아이디와 비밀번호가 일치하지 않습니다.");
                            } else {
                                setErrCode("회원 정보 수정에 실패했습니다.");
                            }
                        }
                        break;
                    case 5:
                        //로그인
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("MISMATCH")) {
                                setErrCode("아이디와 비밀번호가 일치하지 않습니다.");
                            } else if (errcd.equals("EMPTY")) {
                                setErrCode("아이디와 비밀번호를 적어주십시오.");
                            } else {
                                setErrCode("로그인에 실패했습니다.");
                            }
                        }
                        break;
                    case 6:
                        //내 신고내역 조회
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                            if(jsonObject.has("contents")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jo = jsonArray.getJSONObject(i);
                                    int type = Integer.parseInt(jo.getString("type"));
                                    String name = jo.getString("name");
                                    Bitmap photo = null;

                                    if (jo.has("picture")) {
                                        String photoURL = jo.getString("picture");
                                        photo = decodeBase64(photoURL);
                                    }

                                    String missingDate = jo.getString("missing_date");

                                    MyPost mp = new MyPost(id, name, photo, missingDate, type);

                                    myposts.add(mp);
                                }
                            }

                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";

                            MyPost mp = new MyPost("errorOccure", "1", null, "1", 1);
                            myposts.add(mp);
                        }
                        if(!result) {
                            if (errcd.equals("NOID")) {
                                setErrCode("존재하지 않는 ID");
                            } else {
                                setErrCode("신고 내역 조회에 실패했습니다.");
                            }
                        }
                        break;
                    case 7:
                        //내 제보내역 조회
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                            if(jsonObject.has("contents")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jo = jsonArray.getJSONObject(i);
                                    int type = Integer.parseInt(jo.getString("type"));
                                    String ids = jo.getString("post_user_id");
                                    String name = jo.getString("missing_name");
                                    String comment = jo.getString("comment");
                                    String date = jo.getString("written_date");

                                    MyComments mc = new MyComments(ids, name, comment, date, type);

                                    myComments.add(mc);
                                }
                            }

                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";

                            MyComments mc = new MyComments("errorOccure", "1", "1", "1", 1);
                            myComments.add(mc);
                        }
                        if(!result) {
                            if (errcd.equals("NOID")) {
                                setErrCode("존재하지 않는 ID");
                            } else {
                                setErrCode("제보 내역 조회에 실패했습니다.");
                            }
                        }
                        break;
                    case 8:
                        //내 아이목록
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                            if(jsonObject.has("contents")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jo = jsonArray.getJSONObject(i);
                                    String name = jo.getString("name");

                                    myKids.add(name);
                                }
                            }
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";

                            String name = "errorOccure";
                            myKids.add(name);
                        }
                        if(!result) {
                            if (errcd.equals("NOID")) {
                                setErrCode("존재하지 않는 ID");
                            } else {
                                setErrCode("아이 목록 조회에 실패했습니다.");
                            }
                        }
                        break;
                    case 9:
                        //아이 정보 등록
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("NOID")) {
                                setErrCode("존재하지 않는 아이디입니다.");
                            } else if (errcd.equals("NONAME")) {
                                setErrCode("잘못된 이름입니다.");
                            } else if (errcd.equals("EXISTING_NEWNAME")) {
                                setErrCode("중복된 자녀가 존재합니다.");
                            } else {
                                setErrCode("자녀 등록에 실패했습니다.");
                            }
                        }
                        break;
                    case 10 :
                        //아이 정보 수정
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("NOID")) {
                                setErrCode("존재하지 않는 아이디입니다.");
                            } else if (errcd.equals("NONAME")) {
                                setErrCode("잘못된 이름입니다.");
                            } else if (errcd.equals("EXISTING_NEWNAME")) {
                                setErrCode("중복된 자녀가 존재합니다.");
                            } else {
                                setErrCode("자녀 수정에 실패했습니다.");
                            }
                        }
                        break;
                    case 11:
                        //아이 정보 삭제
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("NOID")) {
                                setErrCode("존재하지 않는 아이디입니다.");
                            } else if (errcd.equals("NONAME")) {
                                setErrCode("존재하지 않는 이름입니다.");
                            } else {
                                setErrCode("자녀 삭제에 실패했습니다.");
                            }
                        }
                        break;
                    case 12 :
                        //자녀 상세 조회
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                            if(jsonObject.has("contents")) {
                                JSONObject jsonObject2 = jsonObject.getJSONObject("contents");

                                String name = jsonObject2.getString("name");
                                int age = Integer.parseInt(jsonObject2.getString("age"));
                                Bitmap photo = null;

                                if (jsonObject2.has("photo")) {
                                    String base643 = jsonObject2.getString("photo");
                                    photo = decodeBase64(base643);
                                }
                                String feature = jsonObject2.getString("feature");

                                myKidInfo = new MyKidInfo(name, age, photo, feature);
                            }
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("NOID")) {
                                setErrCode("존재하지 않는 아이디입니다.");
                            } else if (errcd.equals("NONAME")) {
                                setErrCode("잘못된 이름입니다.");
                            } else if (errcd.equals("EXISTING_NEWNAME")) {
                                setErrCode("중복된 자녀가 존재합니다.");
                            } else {
                                setErrCode("자녀 조회에 실패했습니다.");
                            }
                        }
                        break;
                    case 13:
                        //단기 미아 등록
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("NOTTYPE")) {
                                setErrCode("존재하지 않는 타입입니다.");
                            } else if (errcd.equals("NOTTYPE2")) {
                                setErrCode("존재하지 않는 타입입니다.");
                            } else if (errcd.equals("NOID")) {
                                setErrCode("존재하지 않는 아이디입니다.");
                            } else if (errcd.equals("EXISTING_NAME")) {
                                setErrCode("중복된 아이가 존재합니다.");
                            } else {
                                setErrCode("게시글 등록에 실패했습니다.");
                            }
                        }
                        break;
                    case 14:
                        //장기미아 등록
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("NOTTYPE")) {
                                setErrCode("존재하지 않는 타입입니다.");
                            } else if (errcd.equals("NOTTYPE2")) {
                                setErrCode("존재하지 않는 타입입니다.");
                            } else if (errcd.equals("NOID")) {
                                setErrCode("존재하지 않는 아이디입니다.");
                            } else if (errcd.equals("EXISTING_NAME")) {
                                setErrCode("중복된 아이가 존재합니다.");
                            } else {
                                setErrCode("게시글 등록에 실패했습니다.");
                            }
                        }
                        break;
                    case 15:
                        //단기미아 수정
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("NOTTYPE")) {
                                setErrCode("존재하지 않는 타입입니다.");
                            } else if (errcd.equals("NOTTYPE2")) {
                                setErrCode("존재하지 않는 타입입니다.");
                            } else if (errcd.equals("NOID")) {
                                setErrCode("존재하지 않는 아이디입니다.");
                            } else if (errcd.equals("NONAME")) {
                                setErrCode("잘못된 이름입니다.");
                            } else {
                                setErrCode("게시글 수정에 실패했습니다.");
                            }
                        }
                        break;
                    case 16:
                        //장기미아 수정
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("NOTTYPE")) {
                                setErrCode("존재하지 않는 타입입니다.");
                            } else if (errcd.equals("NOTTYPE2")) {
                                setErrCode("존재하지 않는 타입입니다.");
                            } else if (errcd.equals("NOID")) {
                                setErrCode("존재하지 않는 아이디입니다.");
                            } else if (errcd.equals("NONAME")) {
                                setErrCode("존재하지 않는 이름입니다.");
                            } else {
                                setErrCode("게시글 수정에 실패했습니다.");
                            }
                        }
                        break;
                    case 17:
                        //미아 신고 삭제
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("NOID")) {
                                setErrCode("존재하지 않는 아이디입니다.");
                            } else if (errcd.equals("NONAME")) {
                                setErrCode("존재하지 않는 이름입니다.");
                            } else {
                                setErrCode("게시글 삭제에 실패했습니다.");
                            }
                        }
                        break;
                    case 18 :
                        //단기미아 목록
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                            if(jsonObject.has("contents")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jo = jsonArray.getJSONObject(i);
                                    String pid = jo.getString("pid");
                                    String name = jo.getString("name");
                                    int age = Integer.parseInt(jo.getString("age"));
                                    Bitmap pic = null;

                                    if (jo.has("photo1")) {
                                        String str = jo.getString("photo1");
                                        pic = decodeBase64(str);
                                    }

                                    String missingPlace = jo.getString("missing_place");
                                    String missingDate = jo.getString("missing_date");

                                    //String pid, Bitmap pic, String name, int age, String sight, String lossDate
                                    slc.add(new ShortLossChildInfo(pid, pic, name, age, missingPlace, missingDate));
                                }
                            }

                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";

                            String name = "errorOccure";
                            slc.add(new ShortLossChildInfo("1", null, name, 1, "1", "1"));
                        }
                        if(!result) {
                            if (errcd.equals("NOCITY")) {
                                setErrCode("존재하지 않는 도시");
                            } else {
                                setErrCode("게시글 조회에 실패했습니다.");
                            }
                        }
                        break;
                    case 19:
                        //장기미아 목록
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                            if(jsonObject.has("contents")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jo = jsonArray.getJSONObject(i);
                                    int type2 = Integer.parseInt(jo.getString("type"));
                                    String pid = jo.getString("pid");
                                    String name = jo.getString("name");
                                    int age = Integer.parseInt(jo.getString("age"));
                                    Bitmap pic = null;
                                    Bitmap pic2 = null;

                                    if (jo.has("photo1")) {
                                        String str = jo.getString("photo1");
                                        pic = decodeBase64(str);
                                    }
                                    if (jo.has("photo2")) {
                                        String str = jo.getString("photo2");
                                        pic2 = decodeBase64(str);
                                    }

                                    String missingPlace = jo.getString("missing_place");
                                    String missingDate = jo.getString("missing_date");

                                    //String pid, Bitmap pic, Bitmap oldPic, String name, int age, String sight, String lossDate
                                    llc.add(new LongLossChildInfo(pid, pic2, pic, name, age, missingPlace, missingDate));
                                    System.out.println(llc.get(0).getName());
                                }
                            }
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";

                            String name = "errorOccure";
                            llc.add(new LongLossChildInfo("1", null, null, name, 1, "1", "1"));
                        }
                        if(!result) {
                            if (errcd.equals("NOCITY")) {
                                setErrCode("존재하지 않는 도시");
                            } else {
                                setErrCode("게시글 조회에 실패했습니다.");
                            }
                        }
                        break;
                    case 20 :
                        //단기 미아 상세
                        if(jsonObject.getBoolean("result")) {
                            result = true;

                            if(jsonObject.has("contents")) {
                                JSONObject jsonObject2 = jsonObject.getJSONObject("contents");

                                int age = Integer.parseInt(jsonObject2.getString("age"));
                                Bitmap photo = null;
                                if (jsonObject2.has("photo1")) {
                                    String str = jsonObject2.getString("photo1");
                                    photo = decodeBase64(str);
                                }
                                String missingPlace = jsonObject2.getString("missing_place");
                                String missingDate = jsonObject2.getString("missing_date");
                                String feature = jsonObject2.getString("feature");
                                String phone = jsonObject2.getString("phone");
                                String name = jsonObject2.getString("name");

                                System.out.println(missingPlace +" " + missingDate + " " + feature + " " + phone + " " + name);

                                sli = new ShortLossChildInfo(id, photo, name, age, missingPlace, phone, feature, missingDate);
                            }

                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";

                            String name = "errorOccure";
                            sli = new ShortLossChildInfo("1", null, name, 0, "1", "1", "1", "1");
                        }
                        if(!result) {
                            if (errcd.equals("NONAME")) {
                                setErrCode("존재하지 않는 이름입니다.");
                            } else if (errcd.equals("NOPID")) {
                                setErrCode("존재하지 않는 작성자입니다.");
                            } else {
                                setErrCode("게시글 조회에 실패했습니다.");
                            }
                        }
                        break;
                    case 21:
                        //장기미아 상세
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                            if(jsonObject.has("contents")) {
                                JSONObject jsonObject2 = jsonObject.getJSONObject("contents");
                                int age = Integer.parseInt(jsonObject2.getString("age"));
                                Bitmap photo = null;
                                if (jsonObject2.has("photo1")) {
                                    String str = jsonObject2.getString("photo1");
                                    photo = decodeBase64(str);
                                }
                                Bitmap photo2 = null;
                                if (jsonObject2.has("photo2")) {
                                    String str = jsonObject2.getString("photo2");
                                    photo2 = decodeBase64(str);
                                }
                                String missingPlace = jsonObject2.getString("missing_place");
                                String missingDate = jsonObject2.getString("missing_date");
                                String feature = jsonObject2.getString("feature");
                                String phone = jsonObject2.getString("phone");

                                //String pid, Bitmap pic, Bitmap oldPic, String name, int age, String sight, String telNum, String character, String lossDate
                                //String pid, Bitmap pic, String name, int age, String sight, String telNum, String character, String lossDate
                                lli = new LongLossChildInfo(id, photo, photo2, name, age, missingPlace, phone, feature, missingDate);
                            }

                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";

                            String name = "errorOccure";
                            lli = new LongLossChildInfo("1", null, null, name, 0, "1", "1", "1", "1");
                        }
                        if(!result) {
                            if (errcd.equals("NONAME")) {
                                setErrCode("존재하지 않는 이름입니다.");
                            } else if (errcd.equals("NOPID")) {
                                setErrCode("존재하지 않는 작성자입니다.");
                            } else {
                                setErrCode("게시글 조회에 실패했습니다.");
                            }
                        }
                        break;
                    case 22:
                        //미아제보 등록
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("NONAME")) {
                                setErrCode("잘못된 이름입니다.");
                            } else if (errcd.equals("NOPID")) {
                                setErrCode("잘못된 게시자입니다.");
                            } else if (errcd.equals("OVERMAX")) {
                                setErrCode("최대 글자수를 초과했습니다.");
                            } else {
                                setErrCode("댓글 등록에 실패했습니다.");
                            }
                        }
                        break;
                    case 23:
                        //미아제보 수정
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("NONAME")) {
                                setErrCode("잘못된 이름입니다.");
                            } else if (errcd.equals("NOPID")) {
                                setErrCode("잘못된 게시자입니다.");
                            } else if (errcd.equals("OVERMAX")) {
                                setErrCode("최대 글자수를 초과했습니다.");
                            }else if (errcd.equals("NOID")) {
                                setErrCode("잘못된 아이디입니다.");
                            } else {
                                setErrCode("댓글 수정에 실패했습니다.");
                            }
                        }
                        break;
                    case 24:
                        //댓글 등록
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("NONAME")) {
                                setErrCode("잘못된 이름입니다.");
                            } else if (errcd.equals("NOCID")) {
                                setErrCode("잘못된 제보글입니다.");
                            } else if (errcd.equals("NOPID")) {
                                setErrCode("잘못된 게시자입니다.");
                            }else if (errcd.equals("OVERMAX")) {
                                setErrCode("최대 글자수를 초과했습니다.");
                            }  else if (errcd.equals("NOID")) {
                                setErrCode("잘못된 아이디입니다.");
                            } else {
                                setErrCode("댓글 삭제에 실패했습니다.");
                            }
                        }
                        break;
                    case 25:
                        //댓글 조회
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                            if(jsonObject.has("contents")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jo = jsonArray.getJSONObject(i);
                                    String cid = jo.getString("cid");
                                    String usrName = jo.getString("name");
                                    String contents = jo.getString("contents");
                                    String date = jo.getString("date");
                                    Bitmap photo = null;
                                    String id = jo.getString("id");

                                    if (jo.has("photo")) {
                                        String str = jo.getString("photo");
                                        photo = decodeBase64(str);
                                    }

                                    replyInfo r = new replyInfo(id, cid, photo, name, contents, date, usrName);
                                    comments.add(r);
                                }
                            }

                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";
                            replyInfo r = new replyInfo("1", "1", null, "errorOccure", "", "", "");
                            comments.add(r);
                        }
                        if(!result) {
                            if (errcd.equals("NOPID")) {
                                setErrCode("존재하지 않는 게시자입니다.");
                            }else if (errcd.equals("NONAME")) {
                                setErrCode("존재하지 않는 아이입니다.");
                            } else {
                                setErrCode("댓글 조회에 실패했습니다.");
                            }
                        }
                        break;
                    case 26:
                        //사진 비교
                        JSONArray jsonArray2 = jsonObject.getJSONArray("contents");
                        JSONObject jo2 = jsonArray2.getJSONObject(0);

                        if(jsonObject.getBoolean("result")) {
                            result = true;
                            String strs = jo2.getString("Similarity");

                            similarity = jo2.getString("Similarity");

                            if(!similarity.equals("100.0")) similarity = similarity.substring(0,4);
                        }
                        else {
                            result = false;
                            if(jo2.has("err_code")) errcd = jo2.getString("err_code");
                            else errcd = "ERROR";
                        }
                        if(!result) {
                            if (errcd.equals("ERROR")) {
                                setErrCode("사진 비교에 실패했습니다.");
                            }
                        }
                        break;
                    case 27 :
                        //사진 검색
                        System.out.println("여기까지3");
                        if(jsonObject.getBoolean("result")) {
                            result = true;
                            if(jsonObject.has("contents")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                System.out.println(jsonArray);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jo = jsonArray.getJSONObject(i);
                                    String pid = jo.getString("pid");
                                    String name = jo.getString("name");
                                    Bitmap photo = null;

                                    if (jo.has("photo")) {
                                        String str = jo.getString("photo");
                                        photo = decodeBase64(str);
                                    }

                                    System.out.println("여기까지6");
                                    int type = 1;
                                    if(jo.has("major")) type = 0;
                                    String sim = jo.getString("similarity");

                                    if(!sim.equals("100.0")) sim = sim.substring(0,4) + "%";

                                    compareSearchInfo cs = new compareSearchInfo(type, pid, name, photo, sim);
                                    compareList.add(cs);
                                }
                            }
                        }
                        else {
                            result = false;
                            if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                            else errcd = "ERROR";

                            compareSearchInfo cs = new compareSearchInfo(0,"1", "errorOccure", null, "0");
                            compareList.add(cs);
                        }
                        if(!result) {
                            if (errcd.equals("ERROR")) {
                                setErrCode("사진 검색에 실패했습니다.");
                            }
                        }

                        System.out.println("여기까지1=7");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }


            //pDialog.dismiss();
            //return new JSONObject(builder.toString());
        } else {
            Log.e("TAG-error", "Connection Error!");
        }
    }catch (MalformedURLException e){
        e.printStackTrace();
    }catch (IOException e){
        e.printStackTrace();
    }catch(Exception e) {
        e.printStackTrace();
    }finally {
        conn.disconnect();
    }
    }

    /*
    private boolean isWorking = true;

    private ProgressDialog pDialog;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            System.out.println(isWorking);
            if(isWorking && !((Activity)context).isFinishing()) {
                pDialog = new ProgressDialog(context);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setMessage("잠시만 기다려주세요.");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }
            if(!isWorking && pDialog!=null) {
                pDialog.dismiss();
            }
        }
    };

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            isWorking = true;
            handler.sendEmptyMessage(0);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try{
                //Thread.sleep();
                System.out.println(serverURL);
                URL url = new URL(serverURL);
                conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                //
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(15000);

                //
                conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
                int response = conn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObject = new JSONObject(builder.toString());
                    System.out.println(jsonObject);

                    try {
                        switch (whichFunction) {
                            case 1:
                                //회원가입
                                if (jsonObject.getBoolean("result")) {
                                    result = true;
                                } else {
                                    result = false;
                                    if (jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }

                                if(!result) {
                                    if (errcd.equals("EXISTING_ID")) {
                                        setErrCode("이미 존재하는 아이디입니다.");
                                    } else {
                                        setErrCode("회원가입에 실패했습니다.");
                                    }
                                }
                                break;
                            case 2:
                                //회원탈퇴
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("MISMATCH")) {
                                        setErrCode("아이디와 패스워드가 일치하지 않습니다.");
                                    } else {
                                        setErrCode("회원탈퇴에 실패했습니다.");
                                    }
                                }
                                break;
                            case 3:
                                //회원정보 조회
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                    if(jsonObject.has("contents")) {
                                        JSONObject jsonArray = jsonObject.getJSONObject("contents");
                                        String addr = jsonArray.getString("addr");
                                        String name = jsonArray.getString("name");
                                        String phone = jsonArray.getString("phone");
                                        Bitmap photo = null;

                                        if (jsonArray.has("photo")) {
                                            String photoURL = jsonArray.getString("photo");
                                            System.out.println(photoURL);
                                            photo = decodeBase64(photoURL);
                                        }

                                        ui = new UserInfo(id, pw, addr, name, phone, photo);
                                        //String id, String pw, String addr, String name, String phone, Bitmap photo
                                    }

                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                    ui = new UserInfo("errorOccure", "1", "1", "1", null);
                                }
                                if(!result) {
                                    if (errcd.equals("MISMATCH")) {
                                        setErrCode("아이디와 비밀번호가 일치하지 않습니다.");
                                    } else {
                                        setErrCode("회원 정보 조회에 실패했습니다.");
                                    }
                                }
                                break;
                            case 4:
                                //회원정보 수정
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("MISMATCH")) {
                                        setErrCode("아이디와 비밀번호가 일치하지 않습니다.");
                                    } else {
                                        setErrCode("회원 정보 수정에 실패했습니다.");
                                    }
                                }
                                break;
                            case 5:
                                //로그인
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("MISMATCH")) {
                                        setErrCode("아이디와 비밀번호가 일치하지 않습니다.");
                                    } else if (errcd.equals("EMPTY")) {
                                        setErrCode("아이디와 비밀번호를 적어주십시오.");
                                    } else {
                                        setErrCode("로그인에 실패했습니다.");
                                    }
                                }
                                break;
                            case 6:
                                //내 신고내역 조회
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                    if(jsonObject.has("contents")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jo = jsonArray.getJSONObject(i);
                                            int type = Integer.parseInt(jo.getString("type"));
                                            String name = jo.getString("name");
                                            Bitmap photo = null;

                                            if (jo.has("picture")) {
                                                String photoURL = jo.getString("picture");
                                                photo = decodeBase64(photoURL);
                                            }

                                            String missingDate = jo.getString("missing_date");

                                            MyPost mp = new MyPost(id, name, photo, missingDate, type);

                                            myposts.add(mp);
                                        }
                                    }

                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";

                                    MyPost mp = new MyPost("errorOccure", "1", null, "1", 1);
                                    myposts.add(mp);
                                }
                                if(!result) {
                                    if (errcd.equals("NOID")) {
                                        setErrCode("존재하지 않는 ID");
                                    } else {
                                        setErrCode("신고 내역 조회에 실패했습니다.");
                                    }
                                }
                                break;
                            case 7:
                                //내 제보내역 조회
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                    if(jsonObject.has("contents")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jo = jsonArray.getJSONObject(i);
                                            int type = Integer.parseInt(jo.getString("type"));
                                            String ids = jo.getString("post_user_id");
                                            String name = jo.getString("missing_name");
                                            String comment = jo.getString("comment");
                                            String date = jo.getString("written_date");

                                            MyComments mc = new MyComments(ids, name, comment, date, type);

                                            myComments.add(mc);
                                        }
                                    }

                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";

                                    MyComments mc = new MyComments("errorOccure", "1", "1", "1", 1);
                                    myComments.add(mc);
                                }
                                if(!result) {
                                    if (errcd.equals("NOID")) {
                                        setErrCode("존재하지 않는 ID");
                                    } else {
                                        setErrCode("제보 내역 조회에 실패했습니다.");
                                    }
                                }
                                break;
                            case 8:
                                //내 아이목록
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                    if(jsonObject.has("contents")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jo = jsonArray.getJSONObject(i);
                                            String name = jo.getString("name");

                                            myKids.add(name);
                                        }
                                    }
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";

                                    String name = "errorOccure";
                                    myKids.add(name);
                                }
                                if(!result) {
                                    if (errcd.equals("NOID")) {
                                        setErrCode("존재하지 않는 ID");
                                    } else {
                                        setErrCode("아이 목록 조회에 실패했습니다.");
                                    }
                                }
                                break;
                            case 9:
                                //아이 정보 등록
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("NOID")) {
                                        setErrCode("존재하지 않는 아이디입니다.");
                                    } else if (errcd.equals("NONAME")) {
                                        setErrCode("잘못된 이름입니다.");
                                    } else if (errcd.equals("EXISTING_NEWNAME")) {
                                        setErrCode("중복된 자녀가 존재합니다.");
                                    } else {
                                        setErrCode("자녀 등록에 실패했습니다.");
                                    }
                                }
                                break;
                            case 10 :
                                //아이 정보 수정
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("NOID")) {
                                        setErrCode("존재하지 않는 아이디입니다.");
                                    } else if (errcd.equals("NONAME")) {
                                        setErrCode("잘못된 이름입니다.");
                                    } else if (errcd.equals("EXISTING_NEWNAME")) {
                                        setErrCode("중복된 자녀가 존재합니다.");
                                    } else {
                                        setErrCode("자녀 수정에 실패했습니다.");
                                    }
                                }
                                break;
                            case 11:
                                //아이 정보 삭제
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("NOID")) {
                                        setErrCode("존재하지 않는 아이디입니다.");
                                    } else if (errcd.equals("NONAME")) {
                                        setErrCode("존재하지 않는 이름입니다.");
                                    } else {
                                        setErrCode("자녀 삭제에 실패했습니다.");
                                    }
                                }
                                break;
                            case 12 :
                                //자녀 상세 조회
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                    if(jsonObject.has("contents")) {
                                        JSONObject jsonObject2 = jsonObject.getJSONObject("contents");

                                        String name = jsonObject2.getString("name");
                                        int age = Integer.parseInt(jsonObject2.getString("age"));
                                        Bitmap photo = null;

                                        if (jsonObject2.has("photo")) {
                                            String base643 = jsonObject2.getString("photo");
                                            photo = decodeBase64(base643);
                                        }
                                        String feature = jsonObject2.getString("feature");

                                        myKidInfo = new MyKidInfo(name, age, photo, feature);
                                    }
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("NOID")) {
                                        setErrCode("존재하지 않는 아이디입니다.");
                                    } else if (errcd.equals("NONAME")) {
                                        setErrCode("잘못된 이름입니다.");
                                    } else if (errcd.equals("EXISTING_NEWNAME")) {
                                        setErrCode("중복된 자녀가 존재합니다.");
                                    } else {
                                        setErrCode("자녀 조회에 실패했습니다.");
                                    }
                                }
                                break;
                            case 13:
                                //단기 미아 등록
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("NOTTYPE")) {
                                        setErrCode("존재하지 않는 타입입니다.");
                                    } else if (errcd.equals("NOTTYPE2")) {
                                        setErrCode("존재하지 않는 타입입니다.");
                                    } else if (errcd.equals("NOID")) {
                                        setErrCode("존재하지 않는 아이디입니다.");
                                    } else if (errcd.equals("EXISTING_NAME")) {
                                        setErrCode("중복된 아이가 존재합니다.");
                                    } else {
                                        setErrCode("게시글 등록에 실패했습니다.");
                                    }
                                }
                                break;
                            case 14:
                                //장기미아 등록
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("NOTTYPE")) {
                                        setErrCode("존재하지 않는 타입입니다.");
                                    } else if (errcd.equals("NOTTYPE2")) {
                                        setErrCode("존재하지 않는 타입입니다.");
                                    } else if (errcd.equals("NOID")) {
                                        setErrCode("존재하지 않는 아이디입니다.");
                                    } else if (errcd.equals("EXISTING_NAME")) {
                                        setErrCode("중복된 아이가 존재합니다.");
                                    } else {
                                        setErrCode("게시글 등록에 실패했습니다.");
                                    }
                                }
                                break;
                            case 15:
                                //단기미아 수정
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("NOTTYPE")) {
                                        setErrCode("존재하지 않는 타입입니다.");
                                    } else if (errcd.equals("NOTTYPE2")) {
                                        setErrCode("존재하지 않는 타입입니다.");
                                    } else if (errcd.equals("NOID")) {
                                        setErrCode("존재하지 않는 아이디입니다.");
                                    } else if (errcd.equals("NONAME")) {
                                        setErrCode("잘못된 이름입니다.");
                                    } else {
                                        setErrCode("게시글 수정에 실패했습니다.");
                                    }
                                }
                                break;
                            case 16:
                                //장기미아 수정
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("NOTTYPE")) {
                                        setErrCode("존재하지 않는 타입입니다.");
                                    } else if (errcd.equals("NOTTYPE2")) {
                                        setErrCode("존재하지 않는 타입입니다.");
                                    } else if (errcd.equals("NOID")) {
                                        setErrCode("존재하지 않는 아이디입니다.");
                                    } else if (errcd.equals("NONAME")) {
                                        setErrCode("존재하지 않는 이름입니다.");
                                    } else {
                                        setErrCode("게시글 수정에 실패했습니다.");
                                    }
                                }
                                break;
                            case 17:
                                //미아 신고 삭제
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("NOID")) {
                                        setErrCode("존재하지 않는 아이디입니다.");
                                    } else if (errcd.equals("NONAME")) {
                                        setErrCode("존재하지 않는 이름입니다.");
                                    } else {
                                        setErrCode("게시글 삭제에 실패했습니다.");
                                    }
                                }
                                break;
                            case 18 :
                                //단기미아 목록
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                    if(jsonObject.has("contents")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jo = jsonArray.getJSONObject(i);
                                            String pid = jo.getString("pid");
                                            String name = jo.getString("name");
                                            int age = Integer.parseInt(jo.getString("age"));
                                            Bitmap pic = null;

                                            if (jo.has("photo")) {
                                                String str = jo.getString("photo");
                                                pic = decodeBase64(str);
                                            }

                                            String missingPlace = jo.getString("missing_place");
                                            String missingDate = jo.getString("missing_date");

                                            //String pid, Bitmap pic, String name, int age, String sight, String lossDate
                                            slc.add(new ShortLossChildInfo(pid, pic, name, age, missingPlace, missingDate));
                                        }
                                    }

                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";

                                    String name = "errorOccure";
                                    slc.add(new ShortLossChildInfo("1", null, name, 1, "1", "1"));
                                }
                                if(!result) {
                                    if (errcd.equals("NOCITY")) {
                                        setErrCode("존재하지 않는 도시");
                                    } else {
                                        setErrCode("게시글 조회에 실패했습니다.");
                                    }
                                }
                                break;
                            case 19:
                                //장기미아 목록
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                    if(jsonObject.has("contents")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jo = jsonArray.getJSONObject(i);
                                            int type2 = Integer.parseInt(jo.getString("type2"));
                                            String pid = jo.getString("pid");
                                            String name = jo.getString("name");
                                            int age = Integer.parseInt(jo.getString("age"));
                                            Bitmap pic = null;
                                            Bitmap pic2 = null;

                                            if (jo.has("photo1")) {
                                                String str = jo.getString("photo1");
                                                pic = decodeBase64(str);
                                            }
                                            if (jo.has("photo2")) {
                                                String str = jo.getString("photo2");
                                                pic2 = decodeBase64(str);
                                            }

                                            String missingPlace = jo.getString("missing_place");
                                            String missingDate = jo.getString("missing_date");

                                            //String pid, Bitmap pic, Bitmap oldPic, String name, int age, String sight, String lossDate
                                            llc.add(new LongLossChildInfo(pid, pic2, pic, name, age, missingPlace, missingDate));
                                            System.out.println(llc.get(0).getName());
                                        }
                                    }
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";

                                    String name = "errorOccure";
                                    llc.add(new LongLossChildInfo("1", null, null, name, 1, "1", "1"));
                                }
                                if(!result) {
                                    if (errcd.equals("NOCITY")) {
                                        setErrCode("존재하지 않는 도시");
                                    } else {
                                        setErrCode("게시글 조회에 실패했습니다.");
                                    }
                                }
                                break;
                            case 20 :
                                //단기 미아 상세
                                if(jsonObject.getBoolean("result")) {
                                    result = true;

                                    if(jsonObject.has("contents")) {
                                        JSONObject jsonObject2 = jsonObject.getJSONObject("contents");

                                        int age = Integer.parseInt(jsonObject2.getString("age"));
                                        Bitmap photo = null;
                                        if (jsonObject2.has("photo1")) {
                                            String str = jsonObject2.getString("photo1");
                                            photo = decodeBase64(str);
                                        }
                                        String missingPlace = jsonObject2.getString("missing_place");
                                        String missingDate = jsonObject2.getString("missing_date");
                                        String feature = jsonObject2.getString("feature");
                                        String phone = jsonObject2.getString("phone");
                                        String name = jsonObject2.getString("name");

                                        System.out.println(missingPlace +" " + missingDate + " " + feature + " " + phone + " " + name);

                                        sli = new ShortLossChildInfo(id, photo, name, age, missingPlace, phone, feature, missingDate);
                                    }

                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";

                                    String name = "errorOccure";
                                    sli = new ShortLossChildInfo("1", null, name, 0, "1", "1", "1", "1");
                                }
                                if(!result) {
                                    if (errcd.equals("NONAME")) {
                                        setErrCode("존재하지 않는 이름입니다.");
                                    } else if (errcd.equals("NOPID")) {
                                        setErrCode("존재하지 않는 작성자입니다.");
                                    } else {
                                        setErrCode("게시글 조회에 실패했습니다.");
                                    }
                                }
                                break;
                            case 21:
                                //장기미아 상세
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                    if(jsonObject.has("contents")) {
                                        JSONObject jsonObject2 = jsonObject.getJSONObject("contents");
                                        int age = Integer.parseInt(jsonObject2.getString("age"));
                                        Bitmap photo = null;
                                        if (jsonObject2.has("photo1")) {
                                            String str = jsonObject2.getString("photo1");
                                            photo = decodeBase64(str);
                                        }
                                        Bitmap photo2 = null;
                                        if (jsonObject2.has("photo2")) {
                                            String str = jsonObject2.getString("photo2");
                                            photo2 = decodeBase64(str);
                                        }
                                        String missingPlace = jsonObject2.getString("missing_place");
                                        String missingDate = jsonObject2.getString("missing_date");
                                        String feature = jsonObject2.getString("feature");
                                        String phone = jsonObject2.getString("phone");

                                        //String pid, Bitmap pic, Bitmap oldPic, String name, int age, String sight, String telNum, String character, String lossDate
                                        //String pid, Bitmap pic, String name, int age, String sight, String telNum, String character, String lossDate
                                        lli = new LongLossChildInfo(id, photo, photo2, name, age, missingPlace, phone, feature, missingDate);
                                    }

                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";

                                    String name = "errorOccure";
                                    lli = new LongLossChildInfo("1", null, null, name, 0, "1", "1", "1", "1");
                                }
                                if(!result) {
                                    if (errcd.equals("NONAME")) {
                                        setErrCode("존재하지 않는 이름입니다.");
                                    } else if (errcd.equals("NOPID")) {
                                        setErrCode("존재하지 않는 작성자입니다.");
                                    } else {
                                        setErrCode("게시글 조회에 실패했습니다.");
                                    }
                                }
                                break;
                            case 22:
                                //미아제보 등록
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("NONAME")) {
                                        setErrCode("잘못된 이름입니다.");
                                    } else if (errcd.equals("NOPID")) {
                                        setErrCode("잘못된 게시자입니다.");
                                    } else if (errcd.equals("OVERMAX")) {
                                        setErrCode("최대 글자수를 초과했습니다.");
                                    } else {
                                        setErrCode("댓글 등록에 실패했습니다.");
                                    }
                                }
                                break;
                            case 23:
                                //미아제보 수정
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("NONAME")) {
                                        setErrCode("잘못된 이름입니다.");
                                    } else if (errcd.equals("NOPID")) {
                                        setErrCode("잘못된 게시자입니다.");
                                    } else if (errcd.equals("OVERMAX")) {
                                        setErrCode("최대 글자수를 초과했습니다.");
                                    }else if (errcd.equals("NOID")) {
                                        setErrCode("잘못된 아이디입니다.");
                                    } else {
                                        setErrCode("댓글 수정에 실패했습니다.");
                                    }
                                }
                                break;
                            case 24:
                                //댓글 등록
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("NONAME")) {
                                        setErrCode("잘못된 이름입니다.");
                                    } else if (errcd.equals("NOCID")) {
                                        setErrCode("잘못된 제보글입니다.");
                                    } else if (errcd.equals("NOPID")) {
                                        setErrCode("잘못된 게시자입니다.");
                                    }else if (errcd.equals("OVERMAX")) {
                                        setErrCode("최대 글자수를 초과했습니다.");
                                    }  else if (errcd.equals("NOID")) {
                                        setErrCode("잘못된 아이디입니다.");
                                    } else {
                                        setErrCode("댓글 삭제에 실패했습니다.");
                                    }
                                }
                                break;
                            case 25:
                                //댓글 조회
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                    if(jsonObject.has("contents")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jo = jsonArray.getJSONObject(i);
                                            String cid = jo.getString("cid");
                                            String usrName = jo.getString("name");
                                            String contents = jo.getString("contents");
                                            String date = jo.getString("date");
                                            Bitmap photo = null;
                                            String id = jo.getString("id");

                                            if (jo.has("photo")) {
                                                String str = jo.getString("photo");
                                                photo = decodeBase64(str);
                                            }

                                            replyInfo r = new replyInfo(id, cid, photo, name, contents, date, usrName);
                                            comments.add(r);
                                        }
                                    }

                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                    replyInfo r = new replyInfo("1", "1", null, "errorOccure", "", "", "");
                                    comments.add(r);
                                }
                                if(!result) {
                                    if (errcd.equals("NOPID")) {
                                        setErrCode("존재하지 않는 게시자입니다.");
                                    }else if (errcd.equals("NONAME")) {
                                        setErrCode("존재하지 않는 아이입니다.");
                                    } else {
                                        setErrCode("댓글 조회에 실패했습니다.");
                                    }
                                }
                                break;
                            case 26:
                                //사진 비교
                                if(jsonObject.getBoolean("result")) {
                                    result = true;

                                    similarity = Integer.parseInt(jsonObject.getString("similarity"));
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";
                                }
                                if(!result) {
                                    if (errcd.equals("ERROR")) {
                                        setErrCode("사진 비교에 실패했습니다.");
                                    }
                                }
                                break;
                            case 27 :
                                if(jsonObject.getBoolean("result")) {
                                    result = true;
                                    if(jsonObject.has("contents")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("contents");

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jo = jsonArray.getJSONObject(i);
                                            String pid = jo.getString("pid");
                                            String name = jo.getString("name");
                                            Bitmap photo = null;

                                            if (jo.has("photo")) {
                                                String str = jo.getString("photo");
                                                photo = decodeBase64(str);
                                            }

                                            int type = Integer.parseInt(jo.getString("type"));
                                            int sim = Integer.parseInt(jo.getString("similarlily"));

                                            compareSearchInfo cs = new compareSearchInfo(type, pid, name, photo, sim);
                                            compareList.add(cs);
                                        }
                                    }
                                }
                                else {
                                    result = false;
                                    if(jsonObject.has("err_code")) errcd = jsonObject.getString("err_code");
                                    else errcd = "ERROR";

                                    compareSearchInfo cs = new compareSearchInfo(0,"1", "errorOccure", null, 0);
                                    compareList.add(cs);
                                }
                                if(!result) {
                                    if (errcd.equals("ERROR")) {
                                        setErrCode("사진 검색에 실패했습니다.");
                                    }
                                }
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        conn.disconnect();
                    }


                    //pDialog.dismiss();
                    return new JSONObject(builder.toString());
                } else {
                    Log.e("TAG-error", "Connection Error!");
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch(Exception e) {
                e.printStackTrace();
            }finally {
                conn.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            isWorking = false;
            handler.sendEmptyMessage(0);
            //if(pDialog != null) pDialog.dismiss();
            super.onPostExecute(jsonObject);
        }
    }
    */
}
