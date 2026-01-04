package com.project.laundryappui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.project.laundryappui.api.AuthRepository;
import com.project.laundryappui.auth.ChangePasswordActivity;
import com.project.laundryappui.auth.LoginActivity;
import com.project.laundryappui.menu.home.HomeFragment;
import com.project.laundryappui.menu.message.MessageFragment;
import com.project.laundryappui.menu.notification.NotificationFragment;
import com.project.laundryappui.menu.search.SearchFragment;
import com.project.laundryappui.menu.blog.BlogFragment;
import com.project.laundryappui.help.HelpActivity;
import com.project.laundryappui.profile.MyAccountActivity;
import com.project.laundryappui.support.SupportActivity;
import com.project.laundryappui.utils.SessionManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private SessionManager sessionManager;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Khởi tạo SessionManager và AuthRepository
        sessionManager = new SessionManager(this);
        authRepository = new AuthRepository(this);
        
        // Kiểm tra nếu chưa đăng nhập thì chuyển về LoginActivity
        if (!sessionManager.isLoggedIn()) {
            navigateToLogin();
            return;
        }
        
        setContentView(R.layout.activity_main);

        setToolbar();
        initViews();
        initComponentsNavHeader();
        loadFragment(new HomeFragment());
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(0);
    }

    private void initViews() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        // Button đặt vệ sinh
        findViewById(R.id.btn_dat_ve_sinh).setOnClickListener(v -> navigateToShoeBooking());

        /**
         * Menu Navigation Drawer
         **/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(view -> drawer.openDrawer(GravityCompat.START));
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        toggle.syncState();
    }

    /**
     * Fragment
     **/
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    /**
     * Menu Bottom Navigation Drawer
     * */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_menu_home) {
            fragment = new HomeFragment();
        } else if (itemId == R.id.nav_menu_search) {
            fragment = new SearchFragment();
        } else if (itemId == R.id.nav_menu_notification) {
            fragment = new NotificationFragment();
        } else if (itemId == R.id.nav_menu_message) {
            fragment = new MessageFragment();
        }

        return loadFragment(fragment);
    }

    private void initComponentsNavHeader(){
        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setItemIconTintList(null); //disable tint on each icon to use color icon svg
        
        // Get và update nav header với thông tin user
        View headerView = navigationView.getHeaderView(0);
        TextView tvNameHeader = headerView.findViewById(R.id.atv_name_header);
        TextView tvEmailHeader = headerView.findViewById(R.id.tv_email_header);
        
        // Lấy thông tin từ SessionManager
        String userName = sessionManager.getUserName();
        String userEmail = sessionManager.getUserEmail();
        
        // Set text, nếu không có thì dùng default
        if (userName != null && !userName.isEmpty()) {
            tvNameHeader.setText(userName);
        } else {
            tvNameHeader.setText("User");
        }
        
        if (userEmail != null && !userEmail.isEmpty()) {
            tvEmailHeader.setText(userEmail);
        } else {
            tvEmailHeader.setText("user@example.com");
        }
        
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_my_account) {
                    // Navigate to My Account
                    Intent intent = new Intent(MainActivity.this, MyAccountActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_support) {
                    // Navigate to Support
                    Intent intent = new Intent(MainActivity.this, SupportActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_setting) {
                    // Navigate to Change Password
                    Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_blog) {
                    // Navigate to Blog
                    loadFragment(new BlogFragment());
                } else if (itemId == R.id.nav_help) {
                    // Navigate to Help
                    Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_logout) {
                    showLogoutConfirmation();
                    return true;
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            private void Pesan(String pesan) {
                Toast.makeText(MainActivity.this, pesan, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_profile) {
            Uri uri = Uri.parse("https://vesinhgiay24h.com/vi");
            startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, uri), "Choose Browser"));
        }
        return true;
    }

    /**
     * Hiển thị dialog xác nhận đăng xuất
     */
    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.logout_confirmation))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> performLogout())
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Thực hiện đăng xuất với API
     */
    private void performLogout() {
        // Hiển thị loading (optional - có thể dùng ProgressDialog)
        authRepository.logout(new AuthRepository.AuthCallback<String>() {
            @Override
            public void onSuccess(String message) {
                // Đã clear local data trong repository
                // Chuyển về màn hình đăng nhập
                navigateToLogin();
            }

            @Override
            public void onError(String errorMessage) {
                // Dù có lỗi cũng vẫn logout và về màn hình đăng nhập
                navigateToLogin();
            }
        });
    }

    /**
     * Navigate đến ShoeBookingActivity
     */
    private void navigateToShoeBooking() {
        Intent intent = new Intent(this, ShoeBookingActivity.class);
        startActivity(intent);
    }

    /**
     * Navigate đến LoginActivity
     */
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}