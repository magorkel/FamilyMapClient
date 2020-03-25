package com.example.familymapclient.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.familymapclient.Async.LoginTask;
import com.example.familymapclient.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment
{
    private boolean loginSuccess;
    //LoginResult successfulLoginResult; //need to connect to server for this to work
    private boolean registerSuccess;
    //RegisterResult successfulRegisterResult; //need to connect to server

    private EditTextWatcher serverHostWatcher = new EditTextWatcher();
    private EditTextWatcher serverPortWatcher = new EditTextWatcher();
    private EditTextWatcher userNameWatcher = new EditTextWatcher();
    private EditTextWatcher passwordWatcher = new EditTextWatcher();
    private EditTextWatcher firstNameWatcher = new EditTextWatcher();
    private EditTextWatcher lastNameWatcher = new EditTextWatcher();
    private EditTextWatcher emailWatcher = new EditTextWatcher();

    //private EditText ServerHostField;
    //private EditText ServerPortField;
    //private EditText UserNameField;
    //private EditText PasswordField;
    //private EditText FirstNameField;
    //private EditText LastNameField;
    //private EditText EmailField;
    private RadioGroup GenderRadioGroup;
    private Button SignInButton;
    private Button RegisterButton;

    //private String serverHost;
    //private String serverPort;
    //private String userName;
    //private String password;
    //private String firstName;
    //private String lastName;
    //private String email;
    private String gender;

    /*// TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;*/

    public LoginFragment()
    {
        // Required empty public constructor
    }

    /*/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    /*// TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2)
    {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    public static LoginFragment newInstance()
    {
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginSuccess = false;
        registerSuccess = false;

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

        GenderRadioGroup = view.findViewById(R.id.Gender);
        GenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
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
                }
                throw new RuntimeException("something went wrong with the genders");
            }
        });

        SignInButton = view.findViewById(R.id.SIGN_IN);
        SignInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LoginTask loginTask = new LoginTask(serverHostWatcher.getPassOut(), Integer.parseInt(serverPortWatcher.getPassOut()));
                loginTask.execute();
            }
        });

        RegisterButton = view.findViewById(R.id.REGISTER);

        return view;
    }

    private static class EditTextWatcher implements TextWatcher
    {
        private String passOut;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { passOut = s.toString(); }

        @Override
        public void afterTextChanged(Editable s) {}

        public String getPassOut() { return passOut; }
    }

    private void verify(){
        String host = serverHostWatcher.getPassOut();
    }
}
