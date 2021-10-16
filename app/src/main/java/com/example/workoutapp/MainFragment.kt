package com.example.workoutapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.databinding.MainFragmentBinding


class MainFragment : Fragment(),
NotesListAdapter.ListItemListener{

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter:NotesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)
        binding = MainFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        with(binding.recyclerView){
            setHasFixedSize(true)

            requireActivity().title =getString(R.string.app_name)

            val divider = androidx.recyclerview.widget.DividerItemDecoration(
                context, androidx.recyclerview.widget.LinearLayoutManager(context).orientation
            )
            addItemDecoration(divider)
        }
        viewModel.notesList?.observe(viewLifecycleOwner, Observer {
            Log.i("noteLogging", it.toString())
            adapter = NotesListAdapter(it,this@MainFragment)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        })
        binding.floatingActionButton.setOnClickListener{
            editNote(NEW_NOTE_ID)
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuId =
            if(this::adapter.isInitialized&&adapter.selectedNotes.isNotEmpty()
            ){
                R.menu.menu_main_selected_items
            }else{
                R.menu.menu_main
            }
        inflater.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_sample_data -> addSampleData()
            R.id.action_delete-> deleteSelectedNotes()
            R.id.action_delete_all-> deleteAllNotes()
            else-> return super.onOptionsItemSelected(item)
        }
    }

    private fun deleteAllNotes(): Boolean {
        viewModel.deleteAllNotes()
        return true
    }

    private fun deleteSelectedNotes(): Boolean {
        viewModel.deleteNotes(adapter.selectedNotes)
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.selectedNotes.clear()
            requireActivity().invalidateOptionsMenu()
        }, 100)
        return true
    }

    private fun addSampleData(): Boolean {
        viewModel.addSampleDate()
        return true
    }

    override fun editNote(noteId: Int) {
        Log.i(TAG,"onItemClick:received note id $noteId")
        val action = MainFragmentDirections.actionEditNote(noteId)
        findNavController().navigate(action)
    }

    override fun onItemSelectionChanged() {
        requireActivity().invalidateOptionsMenu()
    }
}