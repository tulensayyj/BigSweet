package com.graduation.yau.bigsweet.shop;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.graduation.yau.bigsweet.R;
import com.graduation.yau.bigsweet.base.BaseActivity;
import com.graduation.yau.bigsweet.model.CityModel;
import com.graduation.yau.bigsweet.model.Order;
import com.graduation.yau.bigsweet.model.Product;
import com.graduation.yau.bigsweet.model.ProvinceModel;
import com.graduation.yau.bigsweet.model.Seller;
import com.graduation.yau.bigsweet.model.User;
import com.graduation.yau.bigsweet.person.OrderDetailActivity;
import com.graduation.yau.bigsweet.util.ConvertUtil;
import com.graduation.yau.bigsweet.util.HttpUtil;
import com.graduation.yau.bigsweet.util.JsonUtil;
import com.graduation.yau.bigsweet.util.StartActivityUtil;
import com.graduation.yau.bigsweet.util.TextUtil;
import com.graduation.yau.bigsweet.util.ToastUtil;

import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by YAULEISIM on 2019/4/30.
 */

public class EditOrderActivity extends BaseActivity {

    private Product mProduct;
    private Seller mSeller;
    private EditText mConsigneeEditText, mPhoneEditText, mAddressEditText, mWordsEditText;
    private TextView mSubmitTextView, mPriceSumTextView, mShopNameTextView, mTitleTextView, mSumTextView, mPriceTextView, mAddTextView, mReduceTextView;
    private TextView mChooseAddressTextView;
    private ImageView mPicImageView;
    private int num = 1;
    private Context context = this;
    PickLocationDialog mPickLocationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProduct = (Product) getIntent().getSerializableExtra("product");
        if (mProduct == null) {
            return;
        }
        loadContentLayout(R.layout.activity_edit_order);
        setTitleName(R.string.activity_edit_order_title);
        mPickLocationDialog = new PickLocationDialog(this);
        requestCitiesData();
    }

    @Override
    protected void initView() {
        super.initView();
        mConsigneeEditText = findViewById(R.id.consignee_edit_order_editText);
        mPhoneEditText = findViewById(R.id.phone_edit_order_editText);
        mAddressEditText = findViewById(R.id.address_edit_order_editText);
        mWordsEditText = findViewById(R.id.words_edit_order_editText);

        mChooseAddressTextView = findViewById(R.id.address_choose_edit_order_textView);

        mSubmitTextView = findViewById(R.id.submit_edit_order_textView);
        mPriceSumTextView = findViewById(R.id.price_sum_edit_order_textView);

        mShopNameTextView = findViewById(R.id.shop_name_edit_order_textView);
        mPicImageView = findViewById(R.id.picture_edit_order_imageView);
        mTitleTextView = findViewById(R.id.title_edit_order_textView);
        mPriceTextView = findViewById(R.id.price_edit_order_textView);
        mSumTextView = findViewById(R.id.sum_edit_order_textView);

        mAddTextView = findViewById(R.id.add_edit_order_textView);
        mReduceTextView = findViewById(R.id.reduce_edit_order_textView);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mAddressEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mAddressEditText.setSingleLine(false);
        mAddressEditText.setHorizontallyScrolling(false);

        mTitleTextView.setText(mProduct.getTitle());
        mPriceTextView.setText(getString(R.string.activity_shop_price) + mProduct.getPrice());
        mSumTextView.setText(ConvertUtil.intToString(num));
        Glide.with(EditOrderActivity.this).load(mProduct.getPictureOneUrl()).into(mPicImageView);

        mPriceSumTextView.setText(getString(R.string.activity_shop_price) + mProduct.getPrice() * num);

        BmobQuery<Seller> sellerBmobQuery = new BmobQuery<>();
        sellerBmobQuery.addWhereEqualTo("objectId", mProduct.getSellerId());
        sellerBmobQuery.findObjects(new FindListener<Seller>() {
            @Override
            public void done(List<Seller> object, BmobException e) {
                if (e == null) {
                    if (object.size() == 1) {
                        mSeller = object.get(0);
                        mShopNameTextView.setText(mSeller.getName());
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });

        mAddTextView.setOnClickListener(this);
        mReduceTextView.setOnClickListener(this);
        mSubmitTextView.setOnClickListener(this);
        mChooseAddressTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.add_edit_order_textView:
                mSumTextView.setText(ConvertUtil.intToString(++num));
                mPriceSumTextView.setText(getString(R.string.activity_shop_price) + mProduct.getPrice() * num);
                break;
            case R.id.reduce_edit_order_textView:
                if (num == 1) {
                    ToastUtil.show(this, R.string.activity_edit_order_error, Toast.LENGTH_SHORT, false);
                } else {
                    mSumTextView.setText(ConvertUtil.intToString(--num));
                    mPriceSumTextView.setText(getString(R.string.activity_shop_price) + mProduct.getPrice() * num);
                }
                break;
            case R.id.submit_edit_order_textView:
                doSubmit();
                break;
            case R.id.address_choose_edit_order_textView:
                showLocationDialog();
                break;
            default:
                break;
        }
    }

    private void requestCitiesData() {
        HttpUtil.sendOkHttpRequest("http://192.168.31.58:80/cities.json", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.show(context, R.string.base_note_network_wrong, Toast.LENGTH_SHORT, false);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<ProvinceModel> list = JsonUtil.parseDataToList(response.body().string(), ProvinceModel.class);
                mPickLocationDialog.initData(list);
            }
        });
    }

    private void showLocationDialog() {
        if (mPickLocationDialog.isGetValue) {
            mPickLocationDialog.makeDialog(new PickLocationDialog.AddressPickedListener() {
                @Override
                public void onAddressPicked(ProvinceModel province, CityModel city, String district) {
                    mChooseAddressTextView.setText(province.getName() + "省" + city.getName() + "市" + district);
                }
            }).show();
        } else {
            requestCitiesData();
        }
    }


    private void doSubmit() {
        String consignee = mConsigneeEditText.getText().toString();
        String phone = mPhoneEditText.getText().toString();
        String addressSimple = mChooseAddressTextView.getText().toString();
        String addressDetail = mAddressEditText.getText().toString();
        if (TextUtil.isEmpty(consignee) || TextUtil.isEmpty(phone) || TextUtil.isEmpty(addressSimple) || TextUtil.isEmpty(addressDetail)) {
            ToastUtil.show(this, R.string.activity_edit_order_null, Toast.LENGTH_SHORT, false);
            return;
        }

        final Order order = new Order();
        order.setAddress(addressSimple + "|" + addressDetail);
        order.setConsignee(consignee);
        order.setPhone(phone);
        order.setProductId(mProduct.getObjectId());
        String words = mWordsEditText.getText().toString();
        if (!TextUtil.isEmpty(words)) {
            order.setWords(words);
        }
        order.setSum(mProduct.getPrice() * num);
        order.setUserId(BmobUser.getCurrentUser(User.class).getObjectId());
        order.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mProduct.addSale(num);
                    mProduct.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ToastUtil.show(EditOrderActivity.this, R.string.activity_edit_order_success, Toast.LENGTH_SHORT, true);
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                    finish();
                    StartActivityUtil.goWithOrder(EditOrderActivity.this, OrderDetailActivity.class, order);
                } else {
                    e.printStackTrace();
                    ToastUtil.show(EditOrderActivity.this, R.string.activity_edit_order_fail, Toast.LENGTH_SHORT, false);
                }
            }
        });
    }
}
