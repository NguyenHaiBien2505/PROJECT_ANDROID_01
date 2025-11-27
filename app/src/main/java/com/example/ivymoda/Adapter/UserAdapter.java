package com.example.ivymoda.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.ivymoda.R;
import com.example.ivymoda.Entity.TaiKhoanWithRole;
import com.example.ivymoda.R;

import java.util.List;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private List<TaiKhoanWithRole> list;

    public UserAdapter(Context context, List<TaiKhoanWithRole> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder h;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
            h = new ViewHolder();
            h.tvSTT = convertView.findViewById(R.id.tvSTT);
            h.tvUsername = convertView.findViewById(R.id.tvUsername);
            h.tvHoTen = convertView.findViewById(R.id.tvHoTen);
            h.tvEmail = convertView.findViewById(R.id.tvEmail);
            h.tvVaiTro = convertView.findViewById(R.id.tvVaiTro);
            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }

        TaiKhoanWithRole tk = list.get(position);
        h.tvSTT.setText(String.valueOf(position + 1));
        h.tvUsername.setText(tk.tenDangNhap);
        h.tvHoTen.setText(tk.hoTen);
        h.tvEmail.setText(tk.email);
        h.tvVaiTro.setText(tk.tenVaiTro);

        return convertView;
    }

    static class ViewHolder {
        TextView tvSTT, tvUsername, tvHoTen, tvEmail, tvVaiTro;
    }
}
