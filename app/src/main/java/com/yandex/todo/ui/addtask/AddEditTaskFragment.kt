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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.yandex.todo.R
import com.yandex.todo.data.api.model.task.Importance
import com.yandex.todo.data.api.model.task.Task
import com.yandex.todo.data.api.model.task.TaskModel
import com.yandex.todo.databinding.FragmentAddTaskBinding
import dagger.android.support.DaggerFragment
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class AddEditTaskFragment : DaggerFragment(), DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentAddTaskBinding

    private val args: AddEditTaskFragmentArgs by navArgs()

    private val viewModel by viewModels<AddEditTaskViewModel> { viewModelFactory }

    private val importanceAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            R.layout.piority_type_row,
            resources.getStringArray(R.array.priority_type)
        )
    }
    private var task: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_add_task, container, false)
        binding = FragmentAddTaskBinding.bind(root).apply {
            viewmodel = viewModel
        }
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = arguments?.getSerializable(TaskModel.TASK) as? Task
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.start(args.taskId)
//        setDataFromBundle()
//        binding.apply {
//            closeBtn.setOnClickListener {
//                activity?.supportFragmentManager?.popBackStack()
//            }
//
//            switcher.setOnCheckedChangeListener(onSwitcherStateChanged)
//
//            deleteBtn.setOnClickListener {
//                Toast.makeText(requireContext(), "Удаление пока не работает", Toast.LENGTH_SHORT)
//                    .show()
//            }
//            saveBtn.setOnClickListener {
//                Toast.makeText(requireContext(), "Сохранение пока не работает", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
    }

    private val onSwitcherStateChanged = CompoundButton.OnCheckedChangeListener { _, p1 ->
        if (p1) {
            showDatePickerDialog()
        } else {
            binding.textDeadline.text = ""
        }
    }

    private fun setDataFromBundle() {
        task?.let {
            binding.apply {
                editText.setText(it.text)
//                if (it.deadline < 0) {
//                    switcher.isChecked = true
//                    textDeadline.text = it.deadline.toDate()
//                }
                spinner.adapter = importanceAdapter
//                spinner.setSelection(
//                    when (it.importance) {
//                        "Низкий" -> 1
//                        "Нет" -> 0
//                        "Высокий" -> 2
//                        else -> 0
//                    }
//                )
            }
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
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
        binding.textDeadline.text = currentDateString
    }
}