package dialogs;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.proyecto_eventos.Activity_Menu;
import com.example.proyecto_eventos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import controladores.FirebaseController;

public class IniciarSesionDialog extends DialogFragment {

    private EditText usuario_input, con_input;
    private TextView tv_cuenta_olvidada;
    private Button iniciar_sesion;
    private FirebaseAuth mAuth;
    private FirebaseController fbcontroller ;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_iniciar_sesion, null);

        mAuth = FirebaseAuth.getInstance();
        fbcontroller = new FirebaseController();

        usuario_input = view.findViewById(R.id.usuario_input);
        con_input = view.findViewById(R.id.con_input);
        tv_cuenta_olvidada = view.findViewById(R.id.tv_cuenta_olvidada);
        iniciar_sesion = view.findViewById(R.id.iniciar_sesion);

        iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usuario_input.getText().toString();
                String password = con_input.getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getActivity(), getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!email.contains("@")) {
                    Toast.makeText(getActivity(), getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getActivity(), getString(R.string.short_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    fbcontroller.obtenerRolUsuario(user.getUid(), new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    String rol = document.getString("rol");
                                                    Intent intent = new Intent(getActivity(), Activity_Menu.class);
                                                    intent.putExtra("rolUsuario", rol);
                                                    Toast.makeText(getActivity(), "Inició sesión como "+rol, Toast.LENGTH_SHORT).show();
                                                    startActivity(intent);
                                                    dismiss();
                                                } else {
                                                    Log.d(TAG, "No existe el documento");
                                                }
                                            } else {
                                                Log.d(TAG, "FALLO-> ", task.getException());
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.error_sesion), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        tv_cuenta_olvidada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                RecuperarContrasinalDialog recuperarContrasinalDialog = new RecuperarContrasinalDialog();
                recuperarContrasinalDialog.show(getFragmentManager(), "recuperar_contrasinal_dialog");
            }
        });

        builder.setView(view)
                .setTitle(getString(R.string.iniciar_sesion));

        return builder.create();
    }
}
