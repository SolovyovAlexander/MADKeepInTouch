package com.example.keepintouch.addPersonSection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.keepintouch.R
import com.example.keepintouch.service.Regularity
import com.example.keepintouch.service.addPersonApiService
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddPersonFragment : Fragment() {

    var personId : Int? = null
    var idToken : String? = null

    lateinit var nameInput : EditText
    lateinit var priority : RadioGroup
    lateinit var topPriority : RadioButton
    lateinit var familyPriority : RadioButton
    lateinit var friendsPriority : RadioButton
    lateinit var otherPriority : RadioButton
    lateinit var regularity : Spinner
    lateinit var contact : EditText
    lateinit var saveButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personId = arguments?.get("id")?.toString()?.toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_person, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameInput = view.findViewById(R.id.add_person_name_input)
        priority = view.findViewById(R.id.priority_radio_group)
        priority.check(R.id.top_priority_radio_button)
        topPriority = view.findViewById(R.id.top_priority_radio_button)
        familyPriority = view.findViewById(R.id.family_priority_radio_button)
        friendsPriority = view.findViewById(R.id.friends_priority_radio_button)
        otherPriority = view.findViewById(R.id.other_priority_radio_button)
        regularity = view.findViewById(R.id.reminder_spinner)
        contact = view.findViewById(R.id.add_person_contact_input)
        saveButton = view.findViewById(R.id.save_button)
        saveButton.setOnClickListener(saveButtonListener)

        val mUser = FirebaseAuth.getInstance().currentUser

        mUser?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    idToken = task.result!!.token
                    bindData()
                } else {
                    println("Problem in getting token!!!")
                }
            }
    }

    private fun bindData() {
        if (personId != null) {
            println(idToken)
            println(personId!!)
            addPersonApiService.getPerson(token = "Bearer $idToken", personId = personId!!).enqueue(object: Callback<Person> {
                override fun onFailure(call: Call<Person>, t: Throwable) {
                    println("Failure: " + t.message)
                }
                override fun onResponse(call: Call<Person>, response: Response<Person>) {
                    println(call.request())
                    val person = response.body()!!
                    println(person?.name)
                    nameInput.setText(person?.name)
                    priority.check(when(person?.priority) {
                        0 -> R.id.top_priority_radio_button
                        1 -> R.id.family_priority_radio_button
                        2 -> R.id.friends_priority_radio_button
                        3 -> R.id.other_priority_radio_button
                        else -> R.id.other_priority_radio_button
                    })
                    regularity.setSelection(when(person?.regularity) {
                        Regularity(notification_type = "D", reminder = "09:00:00", week_days = listOf("0", "1", "2", "3", "4", "5", "6")) -> 0
                        Regularity(notification_type = "W", reminder = "09:00:00", times_a_week = 3) -> 1
                        Regularity(notification_type = "W", reminder = "09:00:00", times_a_week = 2) -> 2
                        Regularity(notification_type = "W", reminder = "09:00:00", times_a_week = 1) -> 3
                        Regularity(notification_type = "W", reminder = "09:00:00", times_a_week = 7) -> 4
                        Regularity(notification_type = "M", reminder = "09:00:00", time_of_month = "AN") -> 5
                        else -> 0
                    })
                    contact.setText(person?.contact)
                }
            })
        }
    }

    private val saveButtonListener = { _: View ->
        if (validateFields()) {
            val priority : Int = when(priority.checkedRadioButtonId) {
                R.id.top_priority_radio_button -> 0
                R.id.family_priority_radio_button -> 1
                R.id.friends_priority_radio_button -> 2
                R.id.other_priority_radio_button -> 3
                else -> 0
            }
            sendDataToApi(priority)
        }
    }

    private fun validateFields(): Boolean {
        var validation = true
        if (nameInput.text.isEmpty()) {
            nameInput.error = "Name is required"
            validation = false
        }
        return validation
    }

    private fun sendDataToApi(priority: Int) {
        val regularity = when (regularity.selectedItemPosition) {
            0 -> Regularity(notification_type = "D", reminder = "09:00:00", week_days = listOf("0", "1", "2", "3", "4", "5", "6")) // Daily
            1 -> Regularity(notification_type = "W", reminder = "09:00:00", times_a_week = 3) // Three times a week
            2 -> Regularity(notification_type = "W", reminder = "09:00:00", times_a_week = 2) // Two times a week
            3 -> Regularity(notification_type = "W", reminder = "09:00:00", times_a_week = 1) // Once a week
            4 -> Regularity(notification_type = "W", reminder = "09:00:00", times_a_week = 7) // Once in two weeks
            5 -> Regularity(notification_type = "M", reminder = "09:00:00", time_of_month = "AN") // Once a month
            else -> Regularity(notification_type = "M", reminder = "09:00:00", time_of_month = "AN")
        }
        val person = Person(
            name = nameInput.text.toString(),
            priority = priority,
            regularity = regularity,
            contact = contact.text.toString()
        )
        if (personId != null) {
            println(person.name)
            addPersonApiService.putPerson(token = "Bearer $idToken", person = person, personId = personId!!).enqueue(object: Callback<Person> {
                override fun onFailure(call: Call<Person>, t: Throwable) {
                    println("Failure: " + t.message)
                }
                override fun onResponse(call: Call<Person>, response: Response<Person>) {
                    println(call.request())
                    view?.findNavController()?.navigate(R.id.action_menu_item_add_person_to_menu_item_home)
                }
            })
        } else {
            addPersonApiService.addPerson(token = "Bearer $idToken", person = person).enqueue(object: Callback<Person> {
                override fun onFailure(call: Call<Person>, t: Throwable) {
                    println("Failure: " + t.message)
                }
                override fun onResponse(call: Call<Person>, response: Response<Person>) {
                    println(call.request())
                    view?.findNavController()?.navigate(R.id.action_menu_item_add_person_to_menu_item_home)
                }
            })
        }
    }
}