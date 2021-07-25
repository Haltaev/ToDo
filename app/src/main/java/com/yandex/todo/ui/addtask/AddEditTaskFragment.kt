package com.yandex.todo.ui.addtask

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yandex.todo.App
import com.yandex.todo.R
import com.yandex.todo.data.model.task.Importance
import com.yandex.todo.data.model.task.Task
import com.yandex.todo.databinding.FragmentAddTaskBinding
import com.yandex.todo.util.observe
import com.yandex.todo.util.setTextViewDrawableColor
import com.yandex.todo.util.toSimpleString
import java.util.*
import javax.inject.Inject


class AddEditTaskFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AddEditTaskViewModel> { viewModelFactory }

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private val args: AddEditTaskFragmentArgs by navArgs()

    var deadline = 0L
    private val importanceAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            R.layout.priority_type_row,
            resources.getStringArray(R.array.priority_type)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAndGetTask()

        setSpinner()

        binding.closeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveBtn.setOnClickListener {
            saveTask()
            findNavController().popBackStack()
        }

        binding.switcher.setOnCheckedChangeListener(onSwitcherStateChanged)

        observe(viewModel.task) {
            activateDeleteButton(it)
        }
    }

    private fun activateDeleteButton(task: Task) {
        binding.deleteBtn.apply {
            setTextViewDrawableColor(R.color.red_delete)
            setTextColor(resources.getColor(R.color.red_delete, null))
            setOnClickListener {
                showAlertDialog {
                    viewModel.deleteTask(task.id)
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun saveTask() {
        val currentDate = Date()
        binding.apply {
            val content = editText.text.toString()
            viewModel.saveTask(
                Task(
                    content = if (content.isBlank()) getString(R.string.without_name) else content,
                    importance = getImportance(),
                    isActive = true,
                    deadline = Date(deadline),
                    createdAt = currentDate,
                    updatedAt = currentDate,
                )
            )
        }
    }

    private fun getImportance(): Importance {
        return Importance.values()[binding.spinner.selectedItemPosition]
    }

    private fun observeAndGetTask() {
        observe(viewModel.task) {
            binding.apply {
                editText.setText(it.content)
                setDeadlineOptions(it.deadline)
                spinner.setSelection(it.importance.ordinal)
            }
        }
        viewModel.getTask(args.taskId)
    }

    private fun setDeadlineOptions(deadline: Date) {
        binding.apply {
            if (deadline.time > Date().time) {
                switcher.isChecked = true
                textDeadline.text = deadline.toSimpleString()
            }
        }
    }

    private fun setSpinner() {
        binding.apply {
            spinner.adapter = importanceAdapter
            spinner.setSelection(0)
        }
    }

    private val onSwitcherStateChanged = CompoundButton.OnCheckedChangeListener { _, p1 ->
        if (p1) {
            showDatePickerDialog()
        } else {
            binding.textDeadline.text = ""
        }
    }

    private fun showAlertDialog(deleteTask: () -> Unit) {
        val alertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setMessage(getString(R.string.sure_to_delete_title))
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, getString(R.string.yes)
        ) { dialog, _ ->
            deleteTask.invoke()
            dialog.dismiss()
        }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, getString(R.string.no)
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
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
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        deadline = calendar.timeInMillis
        binding.textDeadline.text = calendar.time.toSimpleString()
    }
}