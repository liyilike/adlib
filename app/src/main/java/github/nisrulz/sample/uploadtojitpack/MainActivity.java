package github.nisrulz.sample.uploadtojitpack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.banner.integrationsdk.activity.ExitActivity;
import com.banner.integrationsdk.dialog.AdDialog;
import com.banner.integrationsdk.dialog.ExitDialog;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

      Intent myIntent = new Intent(this, ExitActivity.class);
      this.startActivity(myIntent);

    new AdDialog(this, R.style.MyDialogStyle).show();
  }
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (((keyCode == KeyEvent.KEYCODE_BACK) || (keyCode == KeyEvent.KEYCODE_HOME)) && event.getRepeatCount() == 0) {
      new ExitDialog(this, R.style.MyDialogStyle).show();
    }
    return false;
  }

}
