package com.example.lyrio.modules.viewModel;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lyrio.R;
import com.example.lyrio.interfaces.LoginResultadoCallBack;
import com.example.lyrio.modules.model.Usuario;
import com.example.lyrio.modules.view.login.LoginActivity;
import com.example.lyrio.modules.view.login.UserCadastroActivity;
import com.example.lyrio.modules.view.login.UserEsqueciMinhaSenha;
import com.example.lyrio.modules.view.menu.TabMenu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.disposables.CompositeDisposable;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<Usuario> usuarioLiveData = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    private Usuario usuario;
    private LoginResultadoCallBack loginResultadoCallBack;
    public final Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button botaoLogin ;
    private TextView registro ;
    private Button buttonFacebook;
    private Button registreComGoogle;
    private TextView esqueceuSenha ;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Usuario> getUsuarioLiveData(){
        return usuarioLiveData;
    }


    public void botaoClicado(View view) {

        usernameEditText= view.findViewById(R.id.emailDigitado);
        passwordEditText = view.findViewById(R.id.senhaLogin);

        registro = view.findViewById(R.id.registreSe);
        buttonFacebook = view.findViewById(R.id.botaoLoginFacebook);
        registreComGoogle = view.findViewById(R.id.botaoLoginGoogle);
        esqueceuSenha = view.findViewById(R.id.esqueceuSenha);

        usernameEditText.setError(null);
        passwordEditText.setError(null);

        if (usernameEditText.getEditableText().toString().equals("")) {
            usernameEditText.setError("Informe seu email");
        } else if (!emailInvalido(usernameEditText.getEditableText().toString())) {
            usernameEditText.setError("e-mail não foi digitado corretamente");
        } else if (passwordEditText.getEditableText().toString().equals("")) {
            passwordEditText.setError("Informe sua senha");
        } else if (senhaValida(passwordEditText.getEditableText().toString())) {
            passwordEditText.setError("senha inválida");
        } else {
            loginResultadoCallBack.logadoComSucesso("Logado Com Sucesso");
        }

    }

    // confirmar se o formato da senha é valido
    private boolean senhaValida(String senha) {
        senha = senha.trim();
        return senha.length() >= 6 && senha.length() < 14 && textPattern.matcher(senha).matches();
    }

    // conferir se o email é invalido

    public static boolean emailInvalido(String email) {
        boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }
        return isEmailIdValid;
    }

}
