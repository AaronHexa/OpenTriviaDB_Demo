package com.example.hexa_aaronlee.opentriviadb_demo.Fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.hexa_aaronlee.opentriviadb_demo.API.CategoryApi
import com.example.hexa_aaronlee.opentriviadb_demo.MySharedPreference.SharedPreference
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.CategoryData
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.InnerObject.TriviaCategory
import com.example.hexa_aaronlee.opentriviadb_demo.R
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.introspect.VisibilityChecker
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main_page.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.*


class MainPageFragment : Fragment() {

    lateinit var mCategoryApi: CategoryApi
    lateinit var mapper: ObjectMapper
    lateinit var retrofit : Retrofit
    lateinit var categoryArray : ArrayList<String>
    lateinit var categoryIdArray : ArrayList<Long>
    lateinit var difficultyArray : ArrayList<String>
    lateinit var typeQuestionArray : ArrayList<String>
    lateinit var typeQuestionIdArray : ArrayList<String>
    lateinit var mySharedPreferences: SharedPreference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mySharedPreferences = SharedPreference(view.context)

        categoryArray = ArrayList()
        categoryIdArray = ArrayList()

        categoryArray.add("Default")
        categoryIdArray.add(0)

        difficultyArray = arrayListOf("Default","Easy","Medium","Hard")
        typeQuestionArray = arrayListOf("Default","True / False","Multiple Choice")
        typeQuestionIdArray = arrayListOf("Default","boolean","multiple")

        RequestCategory()


        nextBtn.setOnClickListener{

            getDataFromSpinner()

            it.findNavController().navigate(R.id.nav_mainPagrFragment_to_questionPageFragment)
        }

        viewCountBtn.setOnClickListener {
            it.findNavController().navigate(R.id.nav_mainPagrFragment_to_viewQuestionPageFragment)
        }

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i("Selected Data", "${categoryArray[position]} / ${categoryIdArray[position]}")
            }

        }

        spinnerDifficulty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i("Selected Data", "${difficultyArray[position]} / ${difficultyArray[position]}")
            }

        }

        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i("Selected Data", "${typeQuestionArray[position]} / ${typeQuestionArray[position]}")
            }

        }
    }

    fun RequestCategory(){

        /*val okHttpClient = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        okHttpClient.addInterceptor(logging)*/

        mapper = ObjectMapper()
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        mapper.visibilityChecker = VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY)

        retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        mCategoryApi = retrofit.create(CategoryApi::class.java)

        val mObservable = mCategoryApi.getAllCategoryData()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CategoryData>
                {
                    override fun onNext(t: CategoryData) {
                        for (i in 0 until t.triviaCategories.size){
                            categoryArray.add(t.triviaCategories[i].name)
                            categoryIdArray.add(t.triviaCategories[i].id)
                            Log.i("Get Data", "${categoryArray[i]} / ${categoryIdArray[i]}")
                        }

                    }

                    override fun onComplete() {
                        Log.i("Get Data", "Complete")
                        setDataSpinner()
                        loadingIndicator.visibility = View.INVISIBLE
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Log.e("Error",e.toString())
                    }

                })
    }

    fun setDataSpinner(){
        val categoryAdapter = ArrayAdapter(view!!.context,android.R.layout.simple_spinner_item,categoryArray)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//Setting the ArrayAdapter data on the Spinner
        spinnerCategory.adapter = categoryAdapter

        val difficultyAdapter = ArrayAdapter(view!!.context,android.R.layout.simple_spinner_item,difficultyArray)
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//Setting the ArrayAdapter data on the Spinner
        spinnerDifficulty.adapter = difficultyAdapter


        val typeQuestionAdapter = ArrayAdapter(view!!.context,android.R.layout.simple_spinner_item,typeQuestionArray)
        typeQuestionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//Setting the ArrayAdapter data on the Spinner
        spinnerType.adapter = typeQuestionAdapter

    }

    fun randomForCategory() : Int{
        val totalSize = categoryArray.size
        val min = 1

        return Random().nextInt((totalSize - min)+1)+min
    }

    fun randomForDifficulty() : Int{
        val totalSize = difficultyArray.size
        val min = 1

        return Random().nextInt((totalSize - min)+1)+min
    }

    fun randomForType() : Int{
        val totalSize = typeQuestionArray.size
        val min = 1

        return Random().nextInt((totalSize - min)+1)+min
    }

    fun getDataFromSpinner(){

        var category = spinnerCategory.selectedItem.toString()
        var difficulty = spinnerDifficulty.selectedItem.toString()
        var typeQuestion = spinnerType.selectedItem.toString()

        var categoryIdPosition = spinnerCategory.selectedItemPosition
        var difficultyIdPosition = spinnerDifficulty.selectedItemPosition
        var typeIdPosition = spinnerType.selectedItemPosition

        var categoryId = categoryIdArray[categoryIdPosition]

        if(categoryIdPosition == 0){
            categoryIdPosition = randomForCategory()
            categoryId = categoryIdArray[categoryIdPosition]
            category = categoryArray[categoryIdPosition]
        }

        if(difficultyIdPosition == 0){
            difficultyIdPosition = randomForDifficulty()
            difficulty = difficultyArray[difficultyIdPosition]
        }

        if(typeIdPosition == 0){
            typeIdPosition = randomForType()

            typeQuestion = typeQuestionIdArray[typeIdPosition]
        }


        mySharedPreferences.setCategory(categoryId.toString())
        mySharedPreferences.setDifficulty(difficulty.toLowerCase())
        mySharedPreferences.setType(typeQuestion)
    }
}

