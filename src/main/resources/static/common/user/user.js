const BASE_URL_PATH = "http://127.0.0.1:8080/"
const NAME_REGEX = /^[가-힣]{1,10}$/;
const PASSWORD_REGEX  = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_])(?=.*[0-9]).{6,20}$")
const NICKNAME_REGEX  = new RegExp("^.{2,20}$")
const PHONE_REGEX = new RegExp("^\\d{3}-\\d{4}-\\d{4}$")
const EMAIL_REGEX = new RegExp("^[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$")
const BIRTH_REGEX = new RegExp("^\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])-[1-4]$")

function onRegister(){

    let f = new FormData(document.querySelectorAll(".signUpForm")[0])

    if(!infoCheck(f)) {
        return;
    }

    $.ajax({
        url : BASE_URL_PATH+"user/register",
        method : 'post',
        contentType: false,
        processData: false,
        data : f,
        success : function (e){
           console.log( e )
        },
        error : function (e){
            console.log( e )
        }

    })
}


function infoCheck(_info){

    if (!NAME_REGEX.test(_info.get("userName"))) {
        alert("이름 재확인")
        return false;
    }
    else if(!PASSWORD_REGEX.test(_info.get("userPassword")) ) {
        alert("비밀번호 재확인")
        return false;
    }
    else if(!NICKNAME_REGEX.test(_info.get("userNickName")) ) {
        alert("닉네임 재확인")
        return false;
    }
    else if(!PHONE_REGEX.test(_info.get("userPhone")) ) {
        alert("폰번호 재확인")
        return false;
    }
    else if (!EMAIL_REGEX.test(_info.get("userEmail"))) {
        alert("이메일 재확인")
        return false
    }
    else if (!BIRTH_REGEX.test(_info.get("userBirth")) ) {
        alert("생년월일 재확인")
        return false;
    }

    return true;
}