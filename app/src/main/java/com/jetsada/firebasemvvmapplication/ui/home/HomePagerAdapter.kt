package com.jetsada.firebasemvvmapplication.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jetsada.firebasemvvmapplication.ui.note.NoteListFragment
import com.jetsada.firebasemvvmapplication.ui.task.TaskListingFragment
import com.jetsada.firebasemvvmapplication.util.HomeTabs

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = HomeTabs.values().size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            HomeTabs.NOTES.index -> NoteListFragment.newInstance(HomeTabs.NOTES.name)
            HomeTabs.TASKS.index -> TaskListingFragment.newInstance(HomeTabs.TASKS.name)
            else -> throw IllegalStateException("Fragment not found")
        }
    }
}