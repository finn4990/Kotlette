package com.Kotlette.ecommerce.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.adapter.AdapterDetail
import com.Kotlette.ecommerce.databinding.FragmentDetailBinding
import com.Kotlette.ecommerce.file.FileManager
import com.Kotlette.ecommerce.item.ItemDetail
import com.Kotlette.ecommerce.item.ItemHome

class DetailFragment : Fragment() {

    private lateinit var adapter : AdapterDetail
    private lateinit var recyclerView : RecyclerView
    private lateinit var detailArrayList : ArrayList<ItemDetail>
    private lateinit var product : ItemHome

    lateinit var comment: Array<String>
    lateinit var iconUser: Array<Int>
    lateinit var vote: Array<Double>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        val data = context?.let { FileManager(it) }

        setFragmentResultListener("Product") { requestKey, bundle ->
            var id = bundle.getInt("bundleId")
            var title = bundle.getString("bundleTitle")
            var price = bundle.getDouble("bundlePrice")
            product = ItemHome(id, title,null, price)
            binding.titleProduct.text = product.title
        }

        binding.buttonRate.setOnClickListener{
            data?.writeToFile("Id.txt", "${product.id}")
            val fragmentManager = getActivity()?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            val myFragment = ReviewFragment()
            fragmentTransaction?.replace(R.id.frame_layout, myFragment)
            fragmentTransaction?.addToBackStack("fragment Review")
            fragmentTransaction?.commit()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()

        //RecycleViewPopular
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        adapter = AdapterDetail(detailArrayList)
        recyclerView.adapter = adapter


    }

    private fun navigateToDetailFragment(){
    }

    private fun dataInitialize() {

        detailArrayList = arrayListOf<ItemDetail>()

        comment = arrayOf(
            "Via Badia 9",
            "Via Tiepolo 15",
            "Piazza Navona 45A",
            "Srada Longevo 8",
            "Viale Regione Siciliana 77",
            "Via Cordova 6",
            "Via M. De Cervantes 2",
            "Piazza A. Arrigo 4",
            "Via Badia 9",
            "Via Tiepolo 15",
            "Piazza Navona 45A",
            "Srada Longevo 8",
            "Viale Regione Siciliana 77",
            "Via Cordova 6",
            "Via M. De Cervantes 2",
            "Piazza A. Arrigo 4"
        )

        vote = arrayOf(
            3.0,
            2.0,
            3.0,
            2.0,
            3.0,
            2.0,
            3.0,
            2.0,
            3.0,
            2.0,
            3.0,
            2.0,
            3.0,
            2.0,
            2.0,
            4.0
        )

        iconUser = arrayOf(
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24
        )

        for(i in comment.indices){

            val detail = ItemDetail(iconUser[i], comment[i], vote[i],)
            detailArrayList.add(detail)
        }

    }
}