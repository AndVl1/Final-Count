package com.techpark.finalcount


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.techpark.finalcount.main.views.activity.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.random.Random

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainTests {
	private val mAuth = FirebaseAuth.getInstance()

	private var mIdlingResource: IdlingResource? = null
	private val mIdlingRegistry = IdlingRegistry.getInstance()

	@Rule
	@JvmField
	var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

	@Before
	fun setUp() {
		mAuth.signInAnonymously()

		if (mIdlingResource != null) {
			mIdlingRegistry.unregister(mIdlingResource)
		}
		mIdlingResource = MainActivityIdlingResource(mActivityTestRule.activity)
		mIdlingRegistry.register(mIdlingResource)
	}

	@After
	fun tearDown() {
		mAuth.signOut()
		mIdlingRegistry.unregister(mIdlingResource)
	}

	private fun clear() {
		val overflowMenuButton = onView(
			allOf(
				withContentDescription("More options"),
				childAtPosition(
					childAtPosition(
						withId(R.id.action_bar),
						1
					),
					0
				),
				isDisplayed()
			)
		)
		overflowMenuButton.perform(click())

		val appCompatTextView = onView(
			allOf(
				withId(R.id.title), withText("Clear"),
				isDisplayed()
			)
		)
		appCompatTextView.perform(click())
	}

	private fun clickFAB() {
		val floatingActionButton = onView(
			allOf(
				withId(R.id.fab),
				isDisplayed()
			)
		)
		floatingActionButton.perform(click())
	}

	private fun typeName(name: String) {
		val textInputEditText = onView(
			allOf(
				withId(R.id.name_textInput),
				isDisplayed()
			)
		)
		textInputEditText.perform(replaceText(name), closeSoftKeyboard())
	}

	private fun typePrice(price: String) {
		val editText = onView(
			allOf(
				withId(R.id.price_textInput),
				isDisplayed()
			)
		)
		editText.perform(replaceText(price), closeSoftKeyboard())
	}

	private fun clickSubmitButton() {
		val button = onView(
			allOf(
				withId(R.id.submit_adding_button), withText("Submit"),
				isDisplayed()
			)
		)
		button.perform(click())
	}

	private fun textMatches(pos: Int, name: String) {
		val textView = onView(
			allOf(
				withId(R.id.purchase_name),
				childAtPosition(
					allOf(
						withId(R.id.history_list_element),
						childAtPosition(
							withId(R.id.purchasesList),
							0
						)
					),
					pos
				),
				isDisplayed()
			)
		)
		textView.check(matches(withText(name)))
	}

	@Test
	fun mainActivityTests() {
		clear()
		val n = 4
		val res = TreeMap<Int, String>()
		for (i in 0..n) {
			clickFAB()
			val name = "abc${Random.nextInt(0, 200)}"
			typeName(name)
			typePrice(Random.nextInt(0, 1000).toString())
			clickSubmitButton()
			res[i] = name
			Thread.sleep(150)
		}
		for ((i, name) in res) {
			textMatches(i, name)
		}
	}

	private fun childAtPosition(
		parentMatcher: Matcher<View>, position: Int
	): Matcher<View> {

		return object : TypeSafeMatcher<View>() {
			override fun describeTo(description: Description) {
				description.appendText("Child at position $position in parent ")
				parentMatcher.describeTo(description)
			}

			public override fun matchesSafely(view: View): Boolean {
				val parent = view.parent
				return parent is ViewGroup && parentMatcher.matches(parent)
						&& view == parent.getChildAt(position)
			}
		}
	}
}
