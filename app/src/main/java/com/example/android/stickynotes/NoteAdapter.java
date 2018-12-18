package com.example.android.stickynotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<Note> objects;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        objects = notes;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    Note getNote(int position) {
        return ((Note) getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_view_item, parent, false);
        }
        Note note = getNote(position);
        ((TextView) view.findViewById(R.id.note_header)).setText(note.getHeader());
        ((TextView) view.findViewById(R.id.note_date_time)).setText(note.getDateTime());
        return view;
    }
}
