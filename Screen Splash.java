 /*____________________________________________________________________________________________
                                    Código dentro del onCreate:
        El siguiente código se debe copiar dentro del método Oncreate. 
        Es necesario que dentro del manifest, se corran los filtros de Intent, o que el activity que contiene
        al splash se cree como main, para que sea lo primero que se ejecuta en la app
    
   ______________________________________________________________________________________________*/

new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        Intent i=new Intent(Splash.this, Login.class);
        startActivity(i);
        finish();
    }
},3500)//son los milisegundos que dura la pantalla;



}