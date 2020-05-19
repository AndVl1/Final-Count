package com.techpark.finalcount


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.techpark.finalcount.auth.views.activity.AuthActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@LargeTest
@RunWith(AndroidJUnit4::class)
class AuthTests {

	private var mIdlingResource: IdlingResource? = null
	private val mIdlingRegistry = IdlingRegistry.getInstance()

	@Rule
	@JvmField
	var mActivityTestRule = ActivityTestRule(AuthActivity::class.java)

	@Before
	fun setUp() {
		if (mIdlingResource != null) {
			mIdlingRegistry.unregister(mIdlingResource)
		}
		mIdlingResource = BaseActivityIdlingResource(mActivityTestRule.activity);
		mIdlingRegistry.register(mIdlingResource)
	}

	@After
	fun tearDown() {
		mIdlingRegistry.unregister(mIdlingResource)
	}

	@Test
	fun existingAccountRegisterTest() {
		setEmail("aaa@bbb.cc")

		setPassword("123456")

		val appCompatButton = onView(
			allOf(
				withId(R.id.submit_button), withText(mActivityTestRule.activity.getString(R.string.register)),
				childAtPosition(
					allOf(
						withId(R.id.auth_line),
						childAtPosition(
							withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
							3
						)
					),
					2
				),
				isDisplayed()
			)
		)
		appCompatButton.perform(click())

		val textView = onView(
			allOf(
				withId(R.id.status_view),
				withText("The email address is already in use by another account."),
				childAtPosition(
					childAtPosition(
						withId(android.R.id.content),
						0
					),
					0
				),
				isDisplayed()
			)
		)
		textView.check(matches(withText("The email address is already in use by another account.")))
	}

	@Test
	fun loginFailTest() {

		val switch_ = onView(
			allOf(
				withId(R.id.auth_type_switch),
				childAtPosition(
					allOf(
						withId(R.id.auth_line),
						childAtPosition(
							withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
							3
						)
					),
					0
				),
				isDisplayed()
			)
		)
		switch_.perform(click())

		setEmail("aaa${Random.nextInt()}@bbb.cc")
		setPassword("123456")

		val appCompatButton = onView(
			allOf(
				withId(R.id.submit_button), withText(mActivityTestRule.activity.getString(R.string.login)),
				childAtPosition(
					allOf(
						withId(R.id.auth_line),
						childAtPosition(
							withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
							3
						)
					),
					2
				),
				isDisplayed()
			)
		)
		appCompatButton.perform(click())

		val textView = onView(
			allOf(
				withId(R.id.status_view),
				withText("There is no user record corresponding to this identifier. The user may have been deleted."),
				childAtPosition(
					childAtPosition(
						withId(android.R.id.content),
						0
					),
					0
				),
				isDisplayed()
			)
		)
		textView.check(matches(withText("There is no user record corresponding to this identifier. The user may have been deleted.")))
	}

	@Test
	fun emptyLoginTest() {

		val appCompatButton = onView(
			allOf(
				withId(R.id.submit_button), withText(mActivityTestRule.activity.getString(R.string.register)),
				childAtPosition(
					allOf(
						withId(R.id.auth_line),
						childAtPosition(
							withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
							3
						)
					),
					2
				),
				isDisplayed()
			)
		)
		appCompatButton.perform(click())

		val textView = onView(
			allOf(
				withId(R.id.status_view),
				withText(mActivityTestRule.activity.getString(R.string.invalid)),
				childAtPosition(
					childAtPosition(
						withId(android.R.id.content),
						0
					),
					0
				),
				isDisplayed()
			)
		)
		textView.check(matches(withText(mActivityTestRule.activity.getString(R.string.invalid))))
	}

	private fun setEmail(email: String) {
		val appCompatEditText = onView(
			allOf(
				withId(R.id.login_input),
				childAtPosition(
					childAtPosition(
						withId(android.R.id.content),
						0
					),
					1
				),
				isDisplayed()
			)
		)
		appCompatEditText.perform(replaceText(email), closeSoftKeyboard())
	}

	private fun setPassword(pwd: String) {
		val appCompatEditText2 = onView(
			allOf(
				withId(R.id.password_input),
				childAtPosition(
					childAtPosition(
						withId(android.R.id.content),
						0
					),
					2
				),
				isDisplayed()
			)
		)
		appCompatEditText2.perform(replaceText(pwd), closeSoftKeyboard())
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

