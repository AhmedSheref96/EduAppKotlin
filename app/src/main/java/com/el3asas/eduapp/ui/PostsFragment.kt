package com.el3asas.eduapp.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.azan.Azan
import com.azan.AzanTimes
import com.azan.Method.Companion.EGYPT_SURVEY
import com.azan.astrologicalCalc.Location
import com.azan.astrologicalCalc.SimpleDate
import com.el3asas.eduapp.R
import com.el3asas.eduapp.background.LocationService
import com.el3asas.eduapp.background.PrayerBGS
import com.el3asas.eduapp.databinding.FragmentPostsBinding
import com.el3asas.eduapp.helpers.PrayProperties
import com.el3asas.eduapp.helpers.SallahAndDiff
import com.el3asas.eduapp.models.Entity
import com.el3asas.eduapp.ui.adapters.PostsAdapter
import com.el3asas.eduapp.view_models.PostsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import java.text.ParseException
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class PostsFragment : Fragment(), View.OnClickListener {
    private var postsViewModel: PostsViewModel? = null
    private lateinit var entities: Array<Entity?>
    private var diffTime: SallahAndDiff? = null
    private val disposable = CompositeDisposable()
    private var bottomSheetPrayTimes: BottomSheetPrayTimes? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_posts, container, false)
        val b = BottomSheetBehavior.from(binding.ISheet.sheet)
        binding.ISheet.cursor.setOnClickListener { view: View? ->
            if (b.state != BottomSheetBehavior.STATE_EXPANDED) b.setState(
                BottomSheetBehavior.STATE_EXPANDED
            ) else b.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
        binding.ISheet.calender.setOnClickListener(this)
        b.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.ISheet.cursor.setImageResource(R.drawable.row_down)
                } else {
                    binding.ISheet.cursor.setImageResource(R.drawable.row_up)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        postsViewModel = ViewModelProvider(this).get(PostsViewModel::class.java)
        binding.setViewModel(postsViewModel)
        binding.setLifecycleOwner(this)
        postsViewModel!!.init(MainActivity.preferences, this)
        binding.ISheet.add.setOnClickListener(this)
        binding.ISheet.showPrev.setOnClickListener(this)
        binding.ISheet.azkarCard.setOnClickListener(this)
        setViewPager()
        binding.ISheet.progress.progress = 100
        MainActivity.latitude.observe(requireActivity()) { aDouble: Double ->
            MainActivity.longitude.observe(requireActivity()) { aDouble1: Double ->
                val today = SimpleDate(GregorianCalendar())
                val loc = Location(aDouble, aDouble1, 2.0, 0)
                val azan = Azan(loc, EGYPT_SURVEY)
                val prayerTimes = azan.getPrayerTimes(today)
                try {
                    getNextSallah(prayerTimes)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                binding.ISheet.prayerTime.setOnClickListener { v: View? ->
                    if (bottomSheetPrayTimes == null) {
                        postsViewModel!!.initPray(prayerTimes)
                        bottomSheetPrayTimes = BottomSheetPrayTimes(
                            MainActivity.preferences,
                            postsViewModel!!.prayItems
                        )
                    }
                    if (!bottomSheetPrayTimes!!.isShow) {
                        bottomSheetPrayTimes!!.show(requireActivity().supportFragmentManager, "00")
                    }
                }
                val editor = MainActivity.preferences.edit()
                editor.putBoolean("locationSaved", true)
                editor.putString("latitude", aDouble.toString() + "")
                editor.putString("longitude", aDouble1.toString() + "")
                editor.apply()
                checkFirstOpen(MainActivity.preferences)
                setProgressTime()
            }
        }
        return binding.getRoot()
    }

    private fun checkFirstOpen(preferences: SharedPreferences) {
        if (preferences.getBoolean("firstOpen", true) || !PrayerBGS.playing_flag) {
            val intent = Intent(requireActivity(), PrayerBGS::class.java)
            intent.putExtra("firstOpen", true)
            ContextCompat.startForegroundService(requireActivity(), intent)
            PrayProperties.inctance.setPrayProperaties(requireActivity(), preferences)
            preferences.edit().putBoolean("firstOpen", false).apply()
        }
    }

    override fun onClick(v: View) {
        if (v === binding!!.ISheet.add) nextQuots else if (v === binding!!.ISheet.showPrev) showPrevPage() else if (v === binding!!.ISheet.azkarCard) try {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_PostesFragment_to_mainAzkarrFragment)
        } catch (ignored: IllegalArgumentException) {
        } else if (v === binding!!.ISheet.calender) try {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_PostesFragment_to_islamicCalenderFragment)
        } catch (ignored: IllegalArgumentException) {
        }
    }

    private fun setViewPager() {
        entities = arrayOfNulls(3)
        val postsAdapter = PostsAdapter(requireActivity(), postsViewModel)
        postsViewModel!!.model1!!.observe(
            requireActivity()
        ) { entity: Entity? ->
            entities[0] = entity
            postsViewModel!!.model2!!.observe(requireActivity()) { entity1: Entity? ->
                entities[1] = entity1
                postsViewModel!!.model3!!.observe(requireActivity()) { entity2: Entity? ->
                    entities[2] = entity2
                    postsAdapter.setList(entities)
                    binding!!.shimmerLayout.visibility = View.GONE
                    binding!!.viewPager22.visibility = View.VISIBLE
                }
            }
        }
        binding!!.viewPager22.adapter = postsAdapter
        binding!!.viewPager22.clipToPadding = false
        binding!!.viewPager22.clipChildren = false
        binding!!.viewPager22.offscreenPageLimit = 3
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page: View, position: Float ->
            val t = 1 - Math.abs(position)
            page.scaleY = .95f + t * .05f
        }
        binding!!.viewPager22.setPageTransformer(transformer)
        binding!!.viewPager22.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                PostsAdapter.pos = position
            }
        })
    }

    @Throws(ParseException::class)
    private fun getNextSallah(azanTimes: AzanTimes) {
        var t = ""
        diffTime = postsViewModel!!.getSallahLoc(azanTimes)
        when (diffTime!!.i) {
            1 -> t = "يتبقى على صلاه الشروق"
            2 -> t = "يتبقى على صلاه الظهر"
            3 -> t = "يتبقى على صلاه العصر"
            4 -> t = "يتبقى على صلاه المغرب"
            5 -> t = "يتبقى على صلاه العشاء"
            0 -> t = "يتبقى على صلاه الفجر"
        }
        binding!!.title.text = t
        binding!!.time.text = diffB2TimesToStr(diffTime!!.diff - diffTime!!.letfTime)
        binding!!.circularProgressBar.progress = getLeftTime(diffTime!!.diff, diffTime!!.letfTime)
    }

    private fun setProgressTime() {
        disposable.add(
            Observable.interval(1, 1, TimeUnit.MINUTES)
                .subscribe(object : Consumer<Long?> {
                    override fun accept(aLong: Long?) {
                        call()
                    }

                    fun call() {
                        diffTime!!.letfTime = diffTime!!.letfTime + 60000
                        binding!!.time.text =
                            diffB2TimesToStr(diffTime!!.diff - diffTime!!.letfTime)
                    }
                }) { e: Throwable? -> Log.d(ContentValues.TAG, "setProgressTime: error") })
    }

    fun diffB2TimesToStr(difference: Long): String {
        val hours = (difference / (1000 * 60 * 60)).toInt()
        val min = (difference - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)
        return String.format(
            Locale.ENGLISH,
            "%02d:%02d",
            if (hours > 0) hours else -hours,
            if (min > 0) min else -min
        )
    }

    private fun getLeftTime(all: Long, left: Long): Float {
        return left.toFloat() / all * 100
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
        if (LocationService.isRunning) {
            val locationService = Intent(requireActivity(), LocationService::class.java)
            requireActivity().stopService(locationService)
        }
    }

    val nextQuots: Unit
        get() {
            val anim = ValueAnimator.ofFloat(1f, .8f)
            anim.duration = 200
            anim.addUpdateListener { animation: ValueAnimator ->
                binding!!.ISheet.add.scaleX = (animation.animatedValue as Float)
                binding!!.ISheet.add.scaleY = (animation.animatedValue as Float)
            }
            anim.startDelay = 0
            anim.repeatCount = 1
            anim.repeatMode = ValueAnimator.REVERSE
            anim.start()
            if (postsViewModel!!.freeQutes) {
                binding!!.viewPager22.visibility = View.GONE
                binding!!.shimmerLayout.visibility = View.VISIBLE
                postsViewModel!!.new
                binding!!.ISheet.progress.progress = 0
                val animation = ObjectAnimator.ofInt(binding!!.ISheet.progress, "progress", 0, 100)
                animation.duration = 3000
                animation.interpolator = DecelerateInterpolator()
                animation.start()
            } else {
                val anim2 = ValueAnimator.ofInt(0, 45)
                anim2.duration = 300
                anim2.addUpdateListener { animation: ValueAnimator ->
                    binding!!.ISheet.addImg.setRotation(
                        (animation.animatedValue as Int).toFloat()
                    )
                }
                anim2.startDelay = 0
                anim2.repeatCount = 1
                anim2.repeatMode = ValueAnimator.REVERSE
                anim2.start()
            }
        }

    fun showPrevPage() {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_PostesFragment_to_previousPostFragment)
    } /*
    private void getAzkar() throws FileNotFoundException {
        String j = loadJSONFromAsset();
        Gson gson = new Gson();
        Type founderListType = new TypeToken<ArrayList<AzkarEntity>>() {
        }.getType();
        List<AzkarEntity> azkarEntity = gson.fromJson(j, founderListType);
        //Data data=gson.fromJson(j,Data.class);
        for (AzkarEntity zekr : azkarEntity) {
            zekr.setZekr(zekr.getZekr().replace("\n", " "));
            zekr.setCategory(zekr.getCategory().replace("\n", " "));
            zekr.setDescription(zekr.getDescription().replace("\n", " "));
            zekr.setReference(zekr.getReference().replace("\n", " "));

        }
        postsViewModel.setAzkarToDb(azkarEntity);
        Log.d(TAG, "getAzkar: -----------------" + azkarEntity.get(0).getZekr());
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = requireActivity().getAssets().open("databases/azkar.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    */

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: FragmentPostsBinding
    }
}