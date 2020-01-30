$(document).ready(() => {
    console.log("Connected to login page successfully");
    loadForm($('#registration-form'),$('#login-form'));

    var createAccountLink = $('#create-account-link');
    var loginLink = $('#sign-in-link');

    createAccountLink.on('click',() => {
        loadForm($('#login-form'),$('#registration-form'))
    });
    loginLink.on('click',() => {
        loadForm($('#registration-form'),$('#login-form'));
    });
});


// Execute user login task
var login = () => {
    var emailField = $('#login_email').val();
    var pwdField = $('#login_pwd').val();

    if (emailField === '' || pwdField === '') {
        alert('Please enter your credentials properly');
    } else {
        window.location = 'dashboard.html';
    }
};

// Execute password reset task
var resetPwd = () => {};

// Create new user account
var createAccount = () => {
    var usernameField = $('#reg_username').val();
    var emailField = $('#reg_email').val();
    var pwdField = $('#reg_pwd').val();
    var confirmPwdField = $('#reg_confirm_pwd').val();

    if (usernameField === '' || emailField === '' || pwdField === '' || confirmPwdField === '') {
        alert('Please enter your credentials properly');
    } else {
        if (pwdField != confirmPwdField) {
            alert('Passwords do not match. Please try again')
        } else {
            alert("Login verified all fields successfully");


            // Clear fields
            clearFields(usernameField,emailField,pwdField,confirmPwdField);   
        }
        
    }
};

// Clear all fields
var clearFields = (i,j,k,l) => {
    i.val('');
    j.val('');
    k.val('');
    l.val('');
};

// Load form
var loadForm = (hiddenForm, shownForm) => { 
    hiddenForm.hide();
    shownForm.show();
 }

var googleLogin = () => {};

var twitterLogin = () => {};

var facebookLogin = () => {};