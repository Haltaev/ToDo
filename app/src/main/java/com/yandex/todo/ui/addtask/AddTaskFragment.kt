package com.yandex.todo.ui.addtask

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.yandex.todo.R
import com.yandex.todo.data.api.model.Task
import kotlinx.android.synthetic.main.fragment_add_task.*
import java.text.DateFormat
import java.util.*


class AddTaskFragment : Fragment(), DatePickerDialog.OnDateSetListener  {

    private val importanceAdapter by lazy {
        ArrayAdapter(requireContext(), R.layout.piority_type_row, resources.getStringArray(R.array.priority_type))
    }
    private var task: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add_task, container, false).apply { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if(bundle != null) {
            task = arguments?.getSerializable(Task.TASK) as? Task
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner.adapter = importanceAdapter
        task?.let {
            edit_text.setText(it.description)
            if(it.deadline.isNotBlank()) {
                switcher.isChecked = true
                text_deadline.text = it.deadline
            }
            if(it.priority.isNotBlank()) {
                spinner.setSelection(importanceAdapter.getPosition(it.priority))
            }
        }
        close_btn.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        switcher.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                showDatePickerDialog()
            } else {
                text_deadline.text = ""
            }
        }

        delete_btn.setOnClickListener {
            Toast.makeText(requireContext(), "Удаление пока не работает", Toast.LENGTH_SHORT)
                .show()
        }

        save_btn.setOnClickListener {
            Toast.makeText(requireContext(), "Сохранение пока не работает", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val currentDateString = DateFormat.getDateInstance().format(calendar.time)
        text_deadline.text = currentDateString
    }
}