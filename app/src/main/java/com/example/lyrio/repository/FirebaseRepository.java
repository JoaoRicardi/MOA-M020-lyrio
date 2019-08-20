package com.example.lyrio.repository;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Completable;

public class FirebaseRepository {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private static final String TAG = "FirebaseRepository";

    public Completable autenticar(String email, String senha) {
        return Completable.create(emitter -> {
            firebaseAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Login com sucesso");
                            emitter.onComplete();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "Falha de login", task.getException());
                            emitter.onError(task.getException());
                        }
                    });

        });
    }





}
