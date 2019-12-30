/*El siguiente código es el que debe copiarse para poder realiar autenticaciones de usuarios mediante el uso de 
la herramienta Auth de firebase.

Es importante aclarar, que previo a estos pasos es necesario configurar los archivos del gladle para poder
insertar las dependencias necesarias para poder tener acceso a las lases necesarias para poder hacer la
autenticacion

Los pasos previos a la inserción del código son:
1) Crear un proyecto en Firebase con la cuaenta de google asociada y ligar dicho proyecto a la app
   Esto tendrá por consecuencia descargar el archivo Json que se debe asociar a la app, y la copia de las 
   dependencias necesarias para acceder a las clases que hacen a la funcionalidad
   
   Pgar e código segmentado tal y omo se indica en los comentarios.*/

     /*____________________________________________________________________________________________
                                    Decraraciones de variables:

      Las siguientes on las variables fundamentales que se deben declarar para poder llamar a los métodos
      de autenticación. A su vez es RECOMENDABLE declarar las suiguientes variables para la interfaz
      boton ingreso, boton egreso(Superpuesto al primero y con alternancia de visibilidad), progressBar
      .El resto de componentes pertenecerán al diseño general del Activity.
      NOTA: Traer todas las librerias necesarias a los encabezados de Import
   ______________________________________________________________________________________________*/

    
    private final static int GOOGLE_SIGN = 123;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    //instancias de los componntes de la pantalla
    private Button btnLogin;
    private ProgressBar progressBar;
    private Button btnLogout;

    
     /*____________________________________________________________________________________________
                                    Código dentro del onCreate:
        El siguiente código se debe copiar dentro del método Oncreate. Se debe tener en cuenta los nombres que 
        se asignó a las instancias de referencias de elementos gráficos (botones y progressBar) para que no hayan
        referencias erroneas.
    
   ______________________________________________________________________________________________*/

     //Inicialización de componentes;
     btnLogin = findViewById(R.id.NOMBREDELCOMPONENTE);
     btnLogout = findViewById(R.id.NOMBREDELCOMPONENTE);
     pregressBar = findViewById(R.id.NOMBREDELCOMPONENTE);

     //Inicialización de instancias de Firebase
       mAuth = FirebaseAuth.getInstance();
       GoogleSignInOptions gso = new GoogleSignInOptions.Builder()
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        //Agregamos el evento del botón Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sigInGoogle();
            }
        });
        //Agregamos el evento del botón Logout

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        if(mAuth.getCurrentUser()!=null){
            FirebaseUser user= mAuth.getCurrentUser();
            updateUI(user);
        }


         /*____________________________________________________________________________________________
                                    Métodos de la clase (fuera y a continuación del onCreate):
        El siguiente código se debe copiar tal cual se encentra referenciado, solo hay que tene atencion en los
        siguientes comentarios donde se puede personalizar ciertas acciones dependiendo de la naturaleza de la 
        app que se está creando
    
   ______________________________________________________________________________________________*/
  
   
   private void sigInGoogle(){
        pogressBar.setVisibility(View.VISIBLE);
        Intent sigIntent =mGoogleSignInClient.getSignInIntent();
        startActivityForResult(sigIntent,GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GOOGLE_SIGN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                    GoogleSignInAccount account=task.getResult(ApiException.class);
                    if(account!=null){
                        fireBaseAuthWhithGoogle(account);
                    }



            }catch (ApiException e){
                e.printStackTrace();
            }


        }

    }

    private void fireBaseAuthWhithGoogle(GoogleSignInAccount account) {
        Log.d("TAG","firebaseAuthWithGoogle: "+account.getId());
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pogressBar.setVisibility(View.INVISIBLE);
                            Log.d("TAG","signin Successful");
                            FirebaseUser user=mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            pogressBar.setVisibility(View.INVISIBLE);
                            Log.d("TAG","Signin Fail",task.getException());
                            Toast.makeText(Login.this,"SIGININ FAIL",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }
    /*El siguiente método tiene por objeto setear las acciones que se toman luego de verificarse la autenticación
    del usuario, por lo que es probable que acá se quiera:
    
    1)insertar registos del usuario logueado en una base de datos local o firebase
    2)Declarar un intent para redirigirnos a otra Activity (no olvidar de cerrar este activity con el método finish())
    3)otras acciones.

    De momento el contenido de este método arroja un Toast que verifica la direción de correo asociada al usuario

    */
    private void updateUI(FirebaseUser user) {
        if(user!=null){
            String email=user.getEmail();
            btnLogin.setVisibility(View.INVISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
            //quitar esto
            Toast.makeText(Login.this, email,Toast.LENGTH_SHORT).show();


        }else{
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.INVISIBLE);
        }
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateUI(null);
            }
        });
    }
}