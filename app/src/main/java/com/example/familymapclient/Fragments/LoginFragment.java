package com.example.familymapclient.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.familymapclient.Async.GetPersonTask;
import com.example.familymapclient.Async.LoginTask;
import com.example.familymapclient.Async.RegisterTask;
import com.example.familymapclient.Async.UserInfoTask;
import com.example.familymapclient.Activities.MainActivity;
import com.example.familymapclient.R;
import com.example.familymapclient.UserInfo;

import shared.Model1.Person;
import shared.Request1.LoginRequest;
import shared.Request1.RegisterRequest;
import shared.Response1.LoginResponse;
import shared.Response1.RegisterResponse;
import shared.Response1.SinglePersonResponse;


/**
 * A simple {@link Fragment} subclass.
 */
//* Use the {@link LoginFragment#newInstance} factory method to
//* create an instance of this fragment.
public class LoginFragment extends Fragment implements LoginTask.Listener, RegisterTask.Listener, GetPersonTask.Listener, UserInfoTask.Listener
{
    private EditTextWatcher serverHostWatcher = new EditTextWatcher();
    private EditTextWatcher serverPortWatcher = new EditTextWatcher();
    private EditTextWatcher userNameWatcher = new EditTextWatcher();
    private EditTextWatcher passwordWatcher = new EditTextWatcher();
    private EditTextWatcher firstNameWatcher = new EditTextWatcher();
    private EditTextWatcher lastNameWatcher = new EditTextWatcher();
    private EditTextWatcher emailWatcher = new EditTextWatcher();

    private String gender;

    private Button signInButton;
    private Button registerButton;

    public UserInfo userInfo = UserInfo.getUserInfo();

    /*public static LoginFragment newInstance()
    {
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        final EditText ServerHostField = view.findViewById(R.id.Server_Host);
        ServerHostField.addTextChangedListener(serverHostWatcher);

        EditText ServerPortField = view.findViewById(R.id.Server_Post);
        ServerPortField.addTextChangedListener(serverPortWatcher);

        EditText UserNameField = view.findViewById(R.id.User_Name);
        UserNameField.addTextChangedListener(userNameWatcher);

        EditText PasswordField = view.findViewById(R.id.Password);
        PasswordField.addTextChangedListener(passwordWatcher);

        EditText FirstNameField = view.findViewById(R.id.First_Name);
        FirstNameField.addTextChangedListener(firstNameWatcher);

        EditText LastNameField = view.findViewById(R.id.Last_Name);
        LastNameField.addTextChangedListener(lastNameWatcher);

        EditText EmailField = view.findViewById(R.id.Email);
        EmailField.addTextChangedListener(emailWatcher);

        RadioGroup genderRadioGroup = view.findViewById(R.id.Gender);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                int genderID = group.getCheckedRadioButtonId();
                switch (genderID)
                {
                    case R.id.Male:
                        gender = "m";
                        break;
                    case R.id.Female:
                        gender = "f";
                        break;
                    default:
                        Log.e("LoginFragment", "somehow managed to click a different gender");
                        //throw new RuntimeException("something went wrong with the genders");
                }
            }
        });

        signInButton = view.findViewById(R.id.SIGN_IN);
        signInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                login();
            }
        });

        registerButton = view.findViewById(R.id.REGISTER);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                register();
            }
        });

        enableDisableButtons();
        return view;
    }

    private class EditTextWatcher implements TextWatcher
    {
        private String passOut = "";
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            passOut = s.toString();
            enableDisableButtons();
        }

        @Override
        public void afterTextChanged(Editable s) {}

        String getPassOut() { return passOut; }
    }

    private void enableDisableButtons()
    {
        //if sign in fields filled
        //make sign in button clickable
        //else not clickable
        if (!serverHostWatcher.getPassOut().equals("") && !serverPortWatcher.getPassOut().equals("") && !userNameWatcher.getPassOut().equals("") && !passwordWatcher.getPassOut().equals(""))
        {
            signInButton.setEnabled(true);
        }
        else
        {
            signInButton.setEnabled(false);
        }

        if (!serverHostWatcher.getPassOut().equals("") && !serverPortWatcher.getPassOut().equals("") && !userNameWatcher.getPassOut().equals("") && !passwordWatcher.getPassOut().equals("") && !firstNameWatcher.getPassOut().equals("") && !lastNameWatcher.getPassOut().equals("") && !emailWatcher.getPassOut().equals(""))
        {
            registerButton.setEnabled(true);
        }
        else
        {
            registerButton.setEnabled(false);
        }
        //same for register
    }

    /*private void verify()
    {
        String host = serverHostWatcher.getPassOut();
    }*/

    @Override
    public void onLoginComplete(LoginResponse loginResponse)
    {
        Log.i("LoginFragment", "got to onLoginComplete");
        //check loginResponse is != null
        if (loginResponse != null)
        {
            if (loginResponse.getSuccess())
            {
                String personID = loginResponse.getPersonID();
                String authToken = loginResponse.getAuthToken();
                try
                {
                    //GetPersonTask getPersonTask = new GetPersonTask(serverHostWatcher.getPassOut(), Integer.parseInt(serverPortWatcher.getPassOut()), this);//this is cast, why doesn't it do it automatically?
                    //getPersonTask.execute(personID, authToken);

                    UserInfoTask userInfoTask = new UserInfoTask(serverHostWatcher.getPassOut(), Integer.parseInt(serverPortWatcher.getPassOut()), this);
                    userInfoTask.execute(personID, authToken);
                    //userInfo = userInfoTask.getUserInfo();


                    //when they register, call fill, then person and event
                    //when they login, just call person and event responses
                    //userInfo = getPersonTask.getUserInfo();
                }
                catch(NumberFormatException e)
                {
                    Toast.makeText(LoginFragment.this.getContext(), "Port needs to be only numbers", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(LoginFragment.this.getContext(), "Login not successful", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(LoginFragment.this.getContext(), "Login empty", Toast.LENGTH_SHORT).show();
        }
        //check that loginResponse is ok - check success
        //show toast display saying what happened

        //loginTask sent response to this function
    }

    @Override
    public void onRegisterComplete(RegisterResponse registerResponse)
    {
        Log.i("LoginFragment", "got to onRegisterComplete");
        if (registerResponse != null)
        {
            if (registerResponse.getSuccess())
            {
                String personID = registerResponse.getPersonID();
                String authToken = registerResponse.getUniqueToken();
                try
                {
                    //GetPersonTask getPersonTask = new GetPersonTask(serverHostWatcher.getPassOut(), Integer.parseInt(serverPortWatcher.getPassOut()), this);//this is cast, why doesn't it do it automatically?
                    //getPersonTask.execute(personID, authToken);
                    UserInfoTask userInfoTask = new UserInfoTask(serverHostWatcher.getPassOut(), Integer.parseInt(serverPortWatcher.getPassOut()), this);
                    userInfoTask.execute(personID, authToken);
                }
                catch(NumberFormatException e)
                {
                    Toast.makeText(LoginFragment.this.getContext(), "Port needs to be only numbers", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(LoginFragment.this.getContext(), "Register successful", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(LoginFragment.this.getContext(), "Register not successful", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(LoginFragment.this.getContext(), "Register empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetPersonComplete(SinglePersonResponse singlePersonResponse)
    {
        if (singlePersonResponse != null)
        {
            if (singlePersonResponse.getSuccess())
            {
                Toast.makeText(LoginFragment.this.getContext(), "Logged in " + singlePersonResponse.getFirstName() + " " + singlePersonResponse.getLastName() + " successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(LoginFragment.this.getContext(), "Failed to find user in onGetPersonComplete", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(LoginFragment.this.getContext(), "Problem talking to server", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUserInfoComplete()
    {
        getActivity().runOnUiThread(new Runnable()
        {
            public void run()
            {
                String personID = userInfo.getPersonID();
                Person person = userInfo.getPerson(personID);
                //toasts
                Toast.makeText(LoginFragment.this.getContext(), "Logged in " + person.getFirstName() + " " + person.getLastName() + " successfully", Toast.LENGTH_SHORT).show();
                //send message to main activity - intent
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(MainActivity.keyGoToMap, true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onUserInfoFailed()
    {
        Toast.makeText(LoginFragment.this.getContext(), "Failed to find user in onGetPersonComplete", Toast.LENGTH_SHORT).show();
    }

    private void login()
    {
        try
        {
            LoginTask loginTask = new LoginTask(serverHostWatcher.getPassOut(), Integer.parseInt(serverPortWatcher.getPassOut()), this);
            LoginRequest loginRequest = new LoginRequest(userNameWatcher.getPassOut(), passwordWatcher.getPassOut());
            loginTask.execute(loginRequest);
        }
        catch (NumberFormatException e)
        {
            Toast.makeText(LoginFragment.this.getContext(), "Port needs to be only numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private void register()
    {
        try
        {
            RegisterTask registerTask = new RegisterTask(serverHostWatcher.getPassOut(), Integer.parseInt(serverPortWatcher.getPassOut()), this);//this is cast, why doesn't it do it automatically?
            RegisterRequest registerRequest = new RegisterRequest(userNameWatcher.getPassOut(), passwordWatcher.getPassOut(), emailWatcher.getPassOut(), firstNameWatcher.getPassOut(), lastNameWatcher.getPassOut(), gender);
            registerTask.execute(registerRequest);
        }
        catch(NumberFormatException e)
        {
            Toast.makeText(LoginFragment.this.getContext(), "Port needs to be only numbers", Toast.LENGTH_SHORT).show();
        }
    }
}
