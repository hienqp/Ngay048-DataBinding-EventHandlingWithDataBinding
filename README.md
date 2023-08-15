---

# DATA BINDING VÀ MVVM

[DataBinding Google Document](https://developer.android.com/topic/libraries/data-binding)

- DataBinding Library là thư viện hỗ trợ, giúp liên kết giao diện UI trong Layout đến các nguồn dữ liệu trong app, bằng cách sử dụng hình thức khai báo trực tiếp trong Layout thay vì viết chương trình điều khiển
- DataBinding Library được Google giới thiệu nhằm hỗ trợ trực tiếp cho mô hình MVVM trong quá trình xây dựng ứng dụng

---

## CONFIGURE DATA BINDING

- để sử dụng DataBinding, trong file __build.gradle Module App__ ta thêm `buildFeatures` và thiết lập `dataBinding true` vào trong thẻ `android`
- __build.gradle (module:App)__

```js
android {

    buildFeatures {
        dataBinding true
    }
}

```

- click __Sync Now__ để Gradle đồng bộ dữ liệu
- tất nhiên, ta chỉ mới enable Data Binding chứ chưa thực hiện thao tác sử dụng nó
- Data Binding thường sử dụng với ViewModel class trong kiến trúc MVVM, vì thế ta sẽ tiến hành khai báo class ViewModel và thực hiện các bước sử dụng Data Binding

---

## VIEWMODEL

- xây dựng 1 class ViewModel, ở đây ta sẽ dựng class __MainViewModel__ để chuyên xử lý logic cho __MainActivity__, có nghĩa là class __MainViewModel__ này sẽ hoàn toàn thay thế __MainActivity__ trong việc sử lý logic
- giả sử class __MainViewModel__ quản lý TextView, có nhiệm vụ khởi tạo 1 đối tượng có TextView, set và get dữ liệu cho TextView
- __MainViewModel.java__

```java
public class MainViewModel {
    private String name;

    public MainViewModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

---

## LAYOUT DÙNG ĐỂ BIND DATA VỚI VIEWMODEL

- ở đây ta sử dụng luôn layout __activity_main__ để bind data giữa layout với đối tượng MainViewModel
- ta chỉnh sửa lại như sau:
  - cấp view group cao nhất là __layout__, bên trong ta khai báo __data__ và __ViewGroup__
  - khai báo sử dụng data (chỉ đến ViewModel được dùng để bind data)
  - khai báo ViewGroup (LinearLayout, FrameLayout, RelativeLayout, ....) nếu cần quản lý nhiều View, hoặc chỉ 1 View
- khai báo thẻ `<layout></layout>` là cấp __ViewGroup__ cao nhất

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <!--  -->
</layout>
```

- khai báo thẻ `<data></data>` bên trong, và chỉ định `<variable />` cho thẻ `data`
- thẻ `variable` có 2 thuộc tính là:
  - `name`: nên đặt trùng tên với __ViewModel__ đang bind data
  - `type`: hay địa chỉ dẫn đến class __ViewModel__

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="MainViewModel"
            type="com.example.databindinginadapterrecyclerviewwithmvvm.MainViewModel" />
    </data>

    <!--  -->
</layout>
```

- tiếp tục, trong `<layout></layout>` khai báo các ViewGroup hoặc View cần binding data

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="MainViewModel"
            type="com.example.databinding.MainViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#E40606"
            android:padding="5dp"
            android:text="@{MainViewModel.name}"
            android:textSize="20sp"
            android:textStyle="bold" />
    </LinearLayout>
</layout>
```

- ở trên ta thấy TextView sẽ dùng để hiển thị thuộc tính `name` từ `MainViewModel` với cú pháp:
  - `@{ViewModel.THUỘC_TÍNH}` : câu lệnh ràng buộc dữ liệu 1 chiều
  - `@={ViewModel.THUỘC_TÍNH}` : câu lệnh ràng buộc dữ liệu 2 chiều
- với cú pháp trên, thì bất cứ khi nào __MainViewModel__ thay đổi thuộc tính __name__ thì ngay lập tức TextView trên __activity_main.xml__ sẽ thay đổi theo

---

## MAIN ACTIVITY CLASS

- để class __MainViewModel__ và layout __activity_main__ có thể liên lạc được với nhau, phải thông qua class data binding được sinh ra khi __enable dataBinding__
- trong __MainActivity.java__ trước khi gọi function `setContentView()` ta khai báo đối tượng binding tương ứng với layout __activity_main.xml__ thì class binding là __ActivityMainBinding.java__
- __MainActivity.java__

```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
```

- khởi tạo đối tượng ViewModel ở đây là __MainViewModel__
- __MainActivity.java__

```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewModel mainViewModel = new MainViewModel("DataBinding MainViewModel In MainActivity");

    }
}
```

- khởi tạo đối tượng class binding thông qua chính class binding gọi đến function `inflate(getLayoutInflater())`
- hoặc thông qua class `DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main )`
- __MainActivity.java__

```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewModel mainViewModel = new MainViewModel("DataBinding MainViewModel In MainActivity");

        // way1: same ViewBinding way
//        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        // way2:
        mActivityMainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

    }
}
```

- thiết lập set dữ liệu ViewModel cho Binding class
- __MainActivity.java__

```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewModel mainViewModel = new MainViewModel("DataBinding MainViewModel In MainActivity");

        // way1: same ViewBinding way
//        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
//        mActivityMainBinding.setMainViewModel(mainViewModel);

        // way2:
        mActivityMainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        mActivityMainBinding.setMainViewModel(mainViewModel);

    }
}
```

- gọi function `setContentView()` và truyền vào chính là đối tượng Binding thực hiện function `getRoot()`
- __MainActivity.java__

```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewModel mainViewModel = new MainViewModel("DataBinding MainViewModel In MainActivity");

        // way1: same ViewBinding way
//        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
//        mActivityMainBinding.setMainViewModel(mainViewModel);

        // way2:
        mActivityMainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        mActivityMainBinding.setMainViewModel(mainViewModel);

        setContentView(mActivityMainBinding.getRoot());
}
```

---

# DATA BINDING & FRAGMENT IN ACTIVITY

- cách sử dụng __DataBinding__ vói __Fragment__ trong __Activity__ cũng tương tự như cách sử dụng trong chính __Activity__
- các bước như sau:
  - __enable DataBinding__ trong __build.gradle module:app__ và __Sync Now__
  - cài đặt 1 class __ViewModel__ chuyên xử lý logic cho __Fragment__
  - thiết lập __layout__ cho __Fragment__ với:
    - thẻ `<layout></layout>` cao nhất
    - thẻ `<data></data>` chứa thẻ `<variable />` với các thuộc tính __name__ (tên của class Fragment ViewModel) và __type__ (đường chỉ đến class Fragment ViewModel)
  - cài đặt class __Fragment__ để thực hiện kết nối bind data giữa class __Fragment ViewModel__ và __layout__ của __Fragment__
- sau khi thực hiện các bước trên thì việc dựng Fragmnet lên là phần việc còn lại của __Activity__ điều khiển __Fragment__
- ở __Activity__ ta thực hiện các bước sau
  - trong layout của __Activity__ thiết kế 1 layout dùng để chứa __Fragment__
  - trong __Activity__ thực hiện gọi __Fragment__

---

## VIEWMODEL FRAGMENT

- giả sử ta sẽ cài đặt 1 Fragment với tên __MyFragment__, thì ta nên cài đặt ViewModel cho Fragment sao cho liên quan đến Fragment đó, ví dụ __MyFragmentViewModel__
- ta lấy ví dụ Fragment sẽ hiển thị 1 TextView, thì ta sẽ cài đặt __MyFragmentViewModel__ quản lý thuộc tính, mà dữ liệu của thuộc tính này sẽ được bind lên layout của Fragmnet
- __MyFragmentViewModel.java__

```java
public class MyFragmentViewModel {
    private String title;

    public MyFragmentViewModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
```

---

## LAYOUT FRAGMENT

- vì layout của Fragment này sẽ được sử dụng __DataBinding__ nên cách cài đặt cũng tương tự như khi sử dụng DataBinding trên Activity
- ta thực hiện các bước sau:
  - tạo 1 file layout
  - thiết lập thẻ cao nhất là `<layout></layout>`
  - trong thẻ __layout__ cài đặt thẻ `<data></data>` và __layout__ thành phần giao diện của Fragment
    - trong thẻ __data__ khai báo thẻ `<variable />` với các thuộc tính __name__ và __type__
      - __name__: tên class ViewModel của Fragment
      - __type__: đường dẫn đến class ViewModel của Fragment
    - __layout__ thành phần giao diện có thể là
      - 1 ViewGroup chứa các View
      - hoặc chỉ có 1 View nếu giao diện của Fragment đơn giản chỉ có 1 View
- __fragment_my.xml__

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <variable
            name="MyFragmentViewModel"
            type="com.example.databindinginadapterrecyclerviewwithmvvm.MyFragmentViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#0B6D7A"
            android:padding="5dp"
            android:text="@{MyFragmentViewModel.title}"
            android:textSize="20sp"
            android:textStyle="bold" />
    </LinearLayout>
</layout>
```

- ở file layout trên ta thấy __TextView__ được ràng buộc dữ liệu với ViewModel của Fragment ở thuộc tính __text__, nghĩa là ViewModel của Fragment mà thay đổi giá trị thuộc tính __title__ thì __TextView__ này cũng sẽ thay đổi theo
- cú pháp ràng buộc dữ liệu:
  - `@{ViewModel.THUỘC_TÍNH}`: ràng buộc dữ liệu 1 chiều
  - `@={ViewModel.THUỘC_TÍNH}`: ràng buộc dữ liệu 2 chiều

---

## FRAGMENT và androidx

- ta có thể cài đặt __Fragment__ theo template của __Android Studio__ hoặc tạo thủ công bằng cách tạo file class __Fragment__ và file __layout__ cho Fragment
- vì ta sử dụng __DataBinding__ của __library androidx__ vì vậy khi khởi tạo __Fragment__ hoặc khi thực hiện __gọi__ Fragment cũng phải sử dụng __androidx__
- ta cài đặt class __MyFragment extends Fragment__ của __androidx library__ sau đó __override__ lại function __onCreateView()__

- __MyFragment.java__

```java
package com.example.databindinginadapterrecyclerviewwithmvvm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
```

- sau khi có được class Fragment, xử lý tính toán thường được xử lý trong __onCreateView__
- mà __onCreateView__ là function trả về là __View__
- ta khai báo 2 đối tượng __View__ và __ViewModel__ cho Fragment
- __MyFragment.java__

```java
public class MyFragment extends Fragment {
    private FragmemtMyBinding mFragmentMyBinding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
```

- trước lệnh `return` trong __onCreateView__, ta khởi tạo:
  - đối tượng class binding bằng:
    - chính class binding đó gọi function __inflate__
    - hoặc class __DataBindingUtil__ gọi function __inflate__
- __MyFragment.java__

```java
public class MyFragment extends Fragment {
    private View view;
    private FragmentMyBinding mFragmentMyBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // way1:
//        mFragmentMyBinding = FragmemtMyBinding.inflate(inflater, container, false);

        // way2:
        mFragmentMyBinding = DataBindingUtil.inflate(inflater, R.layout.fragmemt_my, container, false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
```

- khởi tạo đối tượng ViewModel của Fragment
- __set__ ViewModel cho đối tượng class binding
- thông qua đối tượng class binding sau khi đã gọi __setMyFragmentViewModel__, gọi đến function __getRoot()__ trả về 1 đối tượng View
- cuối cùng lệnh `return` của Fragment chính là đối tượng View
- __MyFragment.java__

```java
public class MyFragment extends Fragment {
    private FragmemtMyBinding mFragmentMyBinding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // way1:
//        mFragmentMyBinding = FragmemtMyBinding.inflate(inflater, container, false);
//        MyFragmentViewModel myFragmentViewModel = new MyFragmentViewModel("DataBinding Fragment MVVM");
//        mFragmentMyBinding.setMyFragmentViewModel(myFragmentViewModel);

        // way2:
        mFragmentMyBinding = DataBindingUtil.inflate(inflater, R.layout.fragmemt_my, container, false);
        MyFragmentViewModel myFragmentViewModel = new MyFragmentViewModel("DataBinding Fragment MVVM");
        mFragmentMyBinding.setMyFragmentViewModel(myFragmentViewModel);

        view = mFragmentMyBinding.getRoot();
        return view;
    }
}
```

---

## DỰNG FRAGMENT TRONG ACTIVITY

- trong layout của Activity ta thêm 1 View dùng để chứa Fragment
- __activity_main.xml__

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <variable
            name="MainViewModel"
            type="com.example.databindinginadapterrecyclerviewwithmvvm.MainViewModel" />
    </data>


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#E40606"
            android:padding="5dp"
            android:text="@{MainViewModel.name}"
            android:textSize="20sp"
            android:textStyle="bold" />

        <FrameLayout
            android:id="@+id/fragment_content"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </LinearLayout>

</layout>
```

- trong __MainActivity__ trước khi gọi lệnh `setContentView()` ta thực hiện gọi hiển thị Fragment lên View chứa Fragment trong Activity
- __MainActivity.java__

```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainViewModel mainViewModel = new MainViewModel("DataBinding MainViewModel In MainActivity");

        // way1: same ViewBinding way
//        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
//        mActivityMainBinding.setMainViewModel(mainViewModel);

        // way2:
        mActivityMainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        mActivityMainBinding.setMainViewModel(mainViewModel);

        buildMyFragment();

        setContentView(mActivityMainBinding.getRoot());
    }

    private void buildMyFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, new MyFragment());
        fragmentTransaction.commitAllowingStateLoss();
    }
}
```

---

# RECYCLER VIEW & ADAPTER WITH DATA BINDING IN ACTIVITY

- trong phần này ta sẽ dựng 1 RecyclerView lên trên 1 Activity
- cấu trúc project cần thực hiện như sau: ví dụ ta cần hiển thị list các USER với name và address
  - class User được thiết kế dùng làm ViewModel
  - layout item dùng để hiển thị các thuộc tính đối tượng User
  - Adapter thực hiện logic liên kết List các User lên RecyclerView

---

## LAYOUT RECYCLER VIEW

- trong __activity_main.xml__, View FrameLayout dùng để chứa Fragment ở bài thực hành trước, ta sẽ thay thế bằng View __RecyclerView__
- __activity_main.xml__

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <variable
            name="MainViewModel"
            type="com.example.databindinginadapterrecyclerviewwithmvvm.MainViewModel" />
    </data>


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#E40606"
            android:padding="5dp"
            android:text="@{MainViewModel.name}"
            android:textSize="20sp"
            android:textStyle="bold" />

        <androidx.recyclerview.widget.RecyclerView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:id="@+id/rcv_items" />

    </LinearLayout>

</layout>
```

---

## VIEW MODEL

- xây dựng class User làm ViewModel
- __UserViewModel.java__

```java
package com.example.databindinginadapterrecyclerviewwithmvvm;

public class UserViewModel {
    private String name;
    private String address;

    public UserViewModel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
```

---

## LAYOUT ITEM USER WITH DATA BINDING

- xây dựng layout hiển thị cho mỗi User được tạo ra, với cài đặt Data Binding
- cú pháp bind data

  - `@{Tên_VIEW_MODEL.THUỘC_TÍNH_VIEWMODEL}` : bind data 1 chiều
  - `@={Tên_VIEW_MODEL.THUỘC_TÍNH_VIEWMODEL}` : bind data 2 chiều

- __item_user.xml__

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <variable
            name="UserViewModel"
            type="com.example.databindinginadapterrecyclerviewwithmvvm.UserViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{UserViewModel.name}"
            android:textSize="16sp"
            android:textStyle="bold" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{UserViewModel.address}"
            android:textSize="15sp" />
    </LinearLayout>

</layout>
```

---

## ADAPTER, VIEW HOLDER, VÀ RECYCLER VIEW

- dựng class __UserAdapter__ và khai báo inner class public extends ViewHolder bên trong
- lưu ý: vì DataBinding sử dụng library androidx, nên khi sử dụng các thành phần ta nên sử dụng library của androidx
- __UserAdapter.java__

```java
public class UserAdapter {

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
```

- trong Constructor của __UserViewHolder__ ta cần trả về `super(View)` là 1 View, nhưng ta đang làm việc với Data Binding trên layout __item_user__, vì vậy thay vì truyền vào 1 View cho Constructor của __UserViewHolder__ ta làm như sau:
  - truyền vào Constructor class Binding của layout __item_user__: __ItemUserBinding.java__ được tạo ra khi cài đặt data binding __build.gradle module:app__
  - trong `super()` ta truyền vào đối tượng của __ItemUserBinding__ được truyền vào Constructor gọi đến `getRoot()` để trả về View cho class __RecyclerView.ViewHolder__

```java
public class UserAdapter {

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public UserViewHolder(@NonNull ItemUserBinding itemUserBinding) {
            super(itemUserBinding.getRoot());
        }
    }
}
```

- sau khi có được class ViewHolder, tiến hành __extends RecyclerView.Adapter__ cho __UserAdapter__ với kiểu tham số là __UserViewHolder__
- đồng thời sau khi thực hiện extends ta sẽ override lại 3 method `onCreateViewHolder(), onBindViewHolder(), getItemCount`

```java
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public UserViewHolder(@NonNull ItemUserBinding itemUserBinding) {
            super(itemUserBinding.getRoot());
        }
    }
}
```

- __UserAdapter__ sẽ quản lý 1 List các __UserViewModel__, vì thế ta sẽ khai báo biến thành viên là __List__ với kiểu tham số là __UserViewModel__, đồng thời tạo Constructor cho __UserAdapter__ với thuộc tính đối tượng __List UserViewModel__

```java
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserViewModel> mListUsers;

    public UserAdapter(List<UserViewModel> mListUsers) {
        this.mListUsers = mListUsers;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public UserViewHolder(@NonNull ItemUserBinding itemUserBinding) {
            super(itemUserBinding.getRoot());
        }
    }
}
```

- ta chỉnh sửa lại 3 method override từ __RecyclerView.Adapter__ sao cho phù hợp với project
- đầu tiên method __getItemCount()__ ta kiểm tra nếu __List__ dữ liệu `!= null` thì trả về kích thước của __List__, ngược lại `return 0`

```java
    @Override
    public int getItemCount() {
        if (mListUsers != null) {
            return mListUsers.size();
        }
        return 0;
    }
```

- tiếp theo là method __onCreateViewHolder()__
- đối với method này ta thấy đối tượng trả về là 1 __UserViewHolder__, mà để có được đối tượng từ Constructor của __UserViewHolder__ thì ta cần truyền vào đối tượng binding đến __UserViewModel__ chính là __ItemUserBinding__ được sinh ra tự động bởi layout __item_user.xml__
- sau khi có đối tượng binding, ta chỉ cần return về Constructor của UserViewHolder và truyền vào đối tượng binding cho Constructor đó

```java
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // way 1 to create object binding
//        ItemUserBinding itemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        // way 2 to create object binding
        ItemUserBinding itemUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_user, parent, false);

        return new UserViewHolder(itemUserBinding);
    }
```

- cuối cùng là method __onBindViewHolder()__
- method này sẽ thực hiện bind __ViewModel__ và __ViewHolder__ với nhau thông qua đối tượng Binding
- mà trong tham số trả về trong __onBindViewHolder()__ gồm __UserViewHolder__ và __position__ (vị trí của phần tử item trong list), ta sẽ sử dụng 2 thành phần này như sau
  - __position__ sẽ được dùng để lấy giá trị __item__ trong __List__ gán cho __UserViewModel__
  - đối với __UserViewHolder__, để bind được ViewModel, ta cần đối tượng binding, mà ta chỉ có được đối tượng __UserViewHolder__, vì vậy ta sẽ sử dụng Constructor của __UserViewHolder__ để tạo ra đối tượng __UserViewHolder__ đi kèm với thuộc tính đối tượng binding mỗi khi __onCreateViewHolder()__ được gọi
- chỉnh sửa lại inner class __UserViewHolder__
  - thêm thuộc tính đối tượng __ItemUserBinding__
  - trong Constructor, gán tham số binding được truyền vào Constructor cho thuộc tính của __UserViewHolder__ mỗi khi được tạo

```java
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBinding itemUserBinding;
        public UserViewHolder(@NonNull ItemUserBinding itemUserBinding) {
            super(itemUserBinding.getRoot());
            this.itemUserBinding = itemUserBinding;
        }
    }
```

- quay trở lại __onBindViewHolder__
  - lấy đối tượng __UserViewModel__ từ __position__
  - thông qua thuộc tính binding của __UserViewHolder__ gọi __setUserViewModel(ViewModel)__

```java
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserViewModel userViewModel = mListUsers.get(position);
        holder.itemUserBinding.setUserViewModel(userViewModel);
    }
```

- như vậy là ta đã hoàn thành thiết lập Adapter, ViewHolder, ViewModel với RecyclerView sử dụng DataBinding
- RecyclerView ban đầu ta thiết lập sẽ hiển thị trên Activity, vì thế quay trở lại MainActivity cài đặt cho việc hiển thị RecyclerView

---

## MAIN ACTIVITY & RECYCLER VIEW

- trong MainActivity
- tạo thủ công 1 List dữ liệu, với các phần tử là __UserViewModel__, vì khi khởi tạo __Adapter__ ta cần truyền vào 1 List dữ liệu các item

```java
    private List<UserViewModel> createListUser() {
        List<UserViewModel> list = new ArrayList<>();

        list.add(new UserViewModel("User 1", "Hoan Kiem, Ha Noi, Viet Nam"));
        list.add(new UserViewModel("User 2", "Hoan Kiem, Ha Noi, Viet Nam"));
        list.add(new UserViewModel("User 3", "Hoan Kiem, Ha Noi, Viet Nam"));
        list.add(new UserViewModel("User 4", "Hoan Kiem, Ha Noi, Viet Nam"));
        list.add(new UserViewModel("User 5", "Hoan Kiem, Ha Noi, Viet Nam"));

        return list;
    }
```

- tạo method thực hiện việc hiển thị RecyclerView, trong method này ta cần thực hiện các bước sau
  - tạo Adapter và truyền vào Adapter này method khởi tạo List item
  - tạo đối tượng RecyclerView thông qua đối tượng binding của Activity tham chiếu đến RecyclerView khai báo trên layout Activity
  - tạo __LinearLayoutManager__
  - gọi __setLayoutManager__ cho RecyclerView
  - cuối cùng __setAdapter__ cho RecyclerView

```java
    private void displayRecyclerView() {
        UserAdapter userAdapter = new UserAdapter(createListUser());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        RecyclerView rcvUser = mActivityMainBinding.rcvItems;
        rcvUser.setLayoutManager(linearLayoutManager);

        // đường phân cách trang trí giữa các item
        DividerItemDecoration itemDecoration = new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL);
        rcvUser.addItemDecoration(itemDecoration);

        rcvUser.setAdapter(userAdapter);
    }
```

---

# EVENT HANDLING WITH DATA BINDING

- xử lý sự kiện click với DataBinding
- khi sử dụng DataBinding, ta có thể viết các biểu thức xử lý sự kiện được gửi đi từ View (ví dụ: method onClick())
- tên của thuộc tính sự kiện được xác định bởi tên của method Listener với 1 vài ngoại lệ (ví dụ: View.OnClickListener có method onClick(), vì vậy thuộc tính cho sự kiện này là android:onClick)
- có 1 số event handler chuyên dụng cho click event mà cần những thuộc tính khác ngoài `android:onClick` để tránh xung đột, có thể sử dụng những thuộc tính sau để tránh xung đột

| Class        | Listener setter                                 | Attribute             |
| ------------ | ----------------------------------------------- | --------------------- |
| SearchView   | setOnSearchClickListener(View.OnClickListener)  | android:onSearchClick |
| ZoomControls | setOnZoomInClickListener(View.OnClickListener)  | android:onZoomIn      |
| ZoomControls | setOnZoomOutClickListener(View.OnClickListener) | android:onZoomOut     |

> note: phải xác định được thuộc tính cho sự kiện để khai báo cho View

> ví dụ: ta có 1 Button, Button sẽ nhận sự kiện click, thì trên View của Button đó nếu muốn nhận sự kiện click ta phải khai báo thuộc tính phù hợp cho Button là ``android:onClick``

- ta có thể sử dụng các cơ chế sau để xự lý sự kiện click View
    - Method references
    - Listener bindings

___
## METHOD REFERENCE

- cơ chế Method Reference là việc khai báo biểu thức thuộc tính trên View tham chiếu đến method Listener trong ViewModel
- khi 1 biểu thức thuộc tính trên View được xác định là Method Reference, DataBinding sẽ liên kết Method Reference và đối tượng sở hữu Listener (ở đây là ViewModel) và đặt Listener đó lên View đích, nếu biểu thức bị đánh giá là ``null``, DataBinding sẽ không tạo trình Listener mà thay vào đó đặt Null Listener
- tương tự như việc gán thuộc tính ``android:onClick`` trên View cho 1 method trên Activity, thì cơ chế Method Reference cũng được liên kết trực tiếp với Method đến đối tượng được Binding của DataBinding (VD: ViewModel), vì vậy, khi khai báo biểu thức, thì nó sẽ được xử lý lập tức ở quá trình compile time, nếu có sai sót thì như Method mà nó Reference không tồn tại, hoặc sai Signature (các tham số) thì thông báo lỗi sẽ xuất hiện ở Compile Time, không đợi đến khi Run Time (build xong app)
- sự khác nhau giữa Method Reference và Listener Binding là:
    - Method Reference việc triển khai Listener khi Data được Binding
    - Listener Binding việc triển khai Listener khi Event được kích hoạt

- CÁCH SỬ DỤNG:
    - ở Method được Reference, tham số của nó bắt buộc chỉ được truyền vào đối tượng View
    - biểu thức khai báo cho thuộc tính của View ở Layout 
    ```
    @{ViewModel::MethodReference}
    ```

- Ví dụ: ở layout ``activity_main`` thì class ViewModel xử lý logic là ``MainViewModel``, ta sẽ dựng 1 View Button trên Activity, khi click vào Button sẽ gọi đến Method khai báo trong MainViewModel
- __MainViewModel__
```java
public class MainViewModel {
    private String name;

    public MainViewModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void showToast1(View view) {
        Toast.makeText(view.getContext(), "Method Reference using View", Toast.LENGTH_SHORT).show();
    }

    public void showLog1(View view) {
        Log.e("MethodReference", "Method Reference No Use View");
    }
}
``` 
- __activity_main__
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <variable
            name="MainViewModel"
            type="com.example.eventhandlingwithdatabinding.MainViewModel" />
    </data>


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#E40606"
            android:padding="5dp"
            android:text="@{MainViewModel.name}"
            android:textSize="20sp"
            android:textStyle="bold" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/rcv_items"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{MainViewModel::showToast1}"
            android:text="showToast1" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{MainViewModel::showLog1}"
            android:text="showLog1" />

    </LinearLayout>

</layout>
```
> Note: Method của class được Binding Data - MainViewModel - phải truyền object ``View`` và chỉ duy nhất tham số object ``View``, cơ chế Method Reference sẽ không hoạt động với những method ta cần truyền tham số để xử lý logic <br/>
> biểu thức ở ``layout`` phải theo cú pháp ``@{MaiViewModel::MethodReference}`` <br/>


- build chương trình, kiểm tra Logcat , filter với tag ``Databinding`` để kiểm tra nội dung khi click vào Button showToast1 và showLog1
___

## LISTENER BINDING

- cơ chế Listener Binding là việc khai báo 1 biểu thức Lambda của thuộc tính trên View, và nó được đánh giá khi có 1 Event xảy ra
- Data Binding luôn tạo ra 1 Listener đặt trên View trong cơ chế này, khi có 1 Event xảy ra, Listener sẽ đánh giá biểu thức Lambda
- Listener Binding cho phép ta run các biểu thức liên kết tùy ý
- trong Method Reference, tham số của method phải khớp với tham số của sự kiện Listener
- trong Listener Binding, chỉ yêu cầu giá trị trả về phải khớp với giá trị trả về dự kiến của event listener (trừ void)

### Listener Binding No Parameter

- sử dụng cơ chế Listener Binding với không tham số truyền vào
- trong MainViewModel ta khai báo 1 method không tham số truyền vào
```java
public class MainViewModel {
    private String name;

    public MainViewModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void showToast1(View view) {
        Toast.makeText(view.getContext(), "Method Reference using View", Toast.LENGTH_SHORT).show();
    }

    public void showLog1(View view) {
        Log.e("MethodReference", "Method Reference No Use View");
    }

    public void showLog2() {
        Log.e("ListenerBinding", "Listener Binding No Param");
    }
}
```

- trong layout của MainActivity ta khai báo Button với biểu thức
```
android:onClick="@{() -> MainViewModel.showLog2()}"
``` 
- chỉ đến __showLog2()__
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <variable
            name="MainViewModel"
            type="com.example.eventhandlingwithdatabinding.MainViewModel" />
    </data>


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#E40606"
            android:padding="5dp"
            android:text="@{MainViewModel.name}"
            android:textSize="20sp"
            android:textStyle="bold" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/rcv_items"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{MainViewModel::showToast1}"
            android:text="showToast1" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{MainViewModel::showLog1}"
            android:text="showLog1" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{() -> MainViewModel.showLog2()}"
            android:text="showLog2" />

    </LinearLayout>

</layout>
```

### Listener Binding With 1 Parameter

- sử dụng cơ chế Listener Binding với tham số cơ bản
- trong MainViewModel khai báo Method với tham số String truyền vào
```java
public class MainViewModel {
    private String name;

    public MainViewModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void showToast1(View view) {
        Toast.makeText(view.getContext(), "Method Reference using View", Toast.LENGTH_SHORT).show();
    }

    public void showLog1(View view) {
        Log.e("MethodReference", "Method Reference No Use View");
    }

    public void showLog2() {
        Log.e("ListenerBinding", "Listener Binding No Param");
    }
    
    public void showLog3(String message) {
        Log.e("ListenerBinding", "Listener Binding With Param: " + message);
    }
}
```

- trong layout MainActivity khai báo 1 Button với thuộc tính 

```
android:onClick="@{() -> MainViewModel.showLog3(MainViewModel.name)}"
```

- đồng thời truyền tham số tương ứng vào
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <variable
            name="MainViewModel"
            type="com.example.eventhandlingwithdatabinding.MainViewModel" />
    </data>


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#E40606"
            android:padding="5dp"
            android:text="@{MainViewModel.name}"
            android:textSize="20sp"
            android:textStyle="bold" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/rcv_items"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{MainViewModel::showToast1}"
            android:text="showToast1" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{MainViewModel::showLog1}"
            android:text="showLog1" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{() -> MainViewModel.showLog2()}"
            android:text="showLog2" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{() -> MainViewModel.showLog3(MainViewModel.name)}"
            android:text="showLog3" />

    </LinearLayout>

</layout>
```

### Listener Binding With 2 Parameter
- sử dụng cơ chế Listener Binding với tham số truyền vào, trong đó có 1 tham số là View, 1 tham số dữ liệu cơ bản
- __MainViewModel__
```java
public class MainViewModel {
    private String name;

    public MainViewModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void showToast1(View view) {
        Toast.makeText(view.getContext(), "Method Reference using View", Toast.LENGTH_SHORT).show();
    }

    public void showLog1(View view) {
        Log.e("MethodReference", "Method Reference No Use View");
    }

    public void showLog2() {
        Log.e("ListenerBinding", "Listener Binding No Param");
    }

    public void showLog3(String message) {
        Log.e("ListenerBinding", "Listener Binding With Param: " + message);
    }

    public void showLog4(View view, String message) {
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
```

- trong layout MainActivity ta khai báo thuộc tính ``onClick`` như sau:
```
android:onClick="@{(view) -> MainViewModel.showLog4(view, MainViewModel.name)}"
``` 
> tham số ``view`` phải được truyền vào ở cả 2 cặp ngoặc tròn

- __activity_main__
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <variable
            name="MainViewModel"
            type="com.example.eventhandlingwithdatabinding.MainViewModel" />
    </data>


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#E40606"
            android:padding="5dp"
            android:text="@{MainViewModel.name}"
            android:textSize="20sp"
            android:textStyle="bold" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/rcv_items"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{MainViewModel::showToast1}"
            android:text="showToast1" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{MainViewModel::showLog1}"
            android:text="showLog1" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{() -> MainViewModel.showLog2()}"
            android:text="showLog2" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{() -> MainViewModel.showLog3(MainViewModel.name)}"
            android:text="showLog3" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{(view) -> MainViewModel.showLog4(view, MainViewModel.name)}"
            android:text="showLog4" />

    </LinearLayout>

</layout>
```

### Listener Binding With Parameter Object

- sử dụng cơ chế Listener Binding gọi đến method có tham số là 1 Object nào đó, ví dụ __UserViewModel__
- nhưng trong layout của __MainActivity__ chỉ đang được Binding đến __MainViewModel__, để có thể bind đến __UserViewModel__ thì ở thẻ ``data`` ta khai báo thêm ``variable`` và trỏ đến __UserViewModel__ cho nó
```xml
    <data>

        <variable
            name="UserViewModel"
            type="com.example.eventhandlingwithdatabinding.UserViewModel" />
        <variable
            name="MainViewModel"
            type="com.example.eventhandlingwithdatabinding.MainViewModel" />
    </data>
```
- trong __MainActivity__ ta phải thiết lập __UserViewModel__ cho class Binding của __activity_main__
- __MainActivity__
```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainViewModel mainViewModel = new MainViewModel("DataBinding MainViewModel In MainActivity");

        // way1: same ViewBinding way
//        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
//        mActivityMainBinding.setMainViewModel(mainViewModel);

        // way2:
        mActivityMainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        mActivityMainBinding.setMainViewModel(mainViewModel);
        mActivityMainBinding.setUserViewModel(new UserViewModel("test ListenerBindig", "Listener Binding With Object Parameter"));

        displayRecyclerView();

        setContentView(mActivityMainBinding.getRoot());
    }
```
- ở __MainViewModel__ khai báo method với tham số truyền vào là Object __UserViewModel__ đã được khai báo và thiết lập trong __MainActivity__
- __MainViewModel__
```java
public class MainViewModel {
    private String name;

    public MainViewModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void showToast1(View view) {
        Toast.makeText(view.getContext(), "Method Reference using View", Toast.LENGTH_SHORT).show();
    }

    public void showLog1(View view) {
        Log.e("MethodReference", "Method Reference No Use View");
    }

    public void showLog2() {
        Log.e("ListenerBinding", "Listener Binding No Param");
    }

    public void showLog3(String message) {
        Log.e("ListenerBinding", "Listener Binding With Param: " + message);
    }

    public void showLog4(View view, String message) {
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showLog5(UserViewModel userViewModel) {
        Log.e("ListenerBinding", userViewModel.getName() + ", " + userViewModel.getAddress());
    }
}
```
- lúc này trong layout MainActivity ta hoàn toàn có thể sử dụng biểu thức thuộc tính ``onClick``
```
android:onClick="@{() -> MainViewModel.showLog4(UserViewModel)}"
```
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <variable
            name="UserViewModel"
            type="com.example.eventhandlingwithdatabinding.UserViewModel" />
        <variable
            name="MainViewModel"
            type="com.example.eventhandlingwithdatabinding.MainViewModel" />
    </data>


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#E40606"
            android:padding="5dp"
            android:text="@{MainViewModel.name}"
            android:textSize="20sp"
            android:textStyle="bold" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/rcv_items"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{MainViewModel::showToast1}"
            android:text="showToast1" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{MainViewModel::showLog1}"
            android:text="showLog1" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{() -> MainViewModel.showLog2()}"
            android:text="showLog2" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{() -> MainViewModel.showLog3(MainViewModel.name)}"
            android:text="showLog3" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{(view) -> MainViewModel.showLog4(view, MainViewModel.name)}"
            android:text="showLog4" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{(view) -> MainViewModel.showLog4(view, MainViewModel.name)}"
            android:text="showLog4" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{() -> MainViewModel.showLog5(UserViewModel)}"
            android:text="showLog5" />
    </LinearLayout>

</layout>
```