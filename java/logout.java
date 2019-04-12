import android.widget.Button;
protected void onCreate(Bundle savedInstanceState) {
loggedOut=(Button)findViewById(R.id.logout);
loggedOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Users.this,Login.class);
                startActivity(intent);
                SharedPreferences.Editor editor = getSharedPreferences("login",MODE_PRIVATE).edit();
                editor.clear().commit();
            }
        });
        }
