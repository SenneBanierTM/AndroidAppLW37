import android.content.Context
import android.content.SharedPreferences

object TagManager {
    private const val PREFS_NAME = "tags_prefs"
    private const val TAGS_KEY = "accepted_tags"

    fun getTags(context: Context): MutableSet<String> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet(TAGS_KEY, mutableSetOf("[BK1]", "[UNV]", "[pHr]")) ?: mutableSetOf()
    }

    fun addTag(context: Context, tag: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val tags = getTags(context)
        tags.add(tag)
        sharedPreferences.edit().putStringSet(TAGS_KEY, tags).apply()
    }

    fun removeTag(context: Context, tag: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val tags = getTags(context)
        tags.remove(tag)
        sharedPreferences.edit().putStringSet(TAGS_KEY, tags).apply()
    }
}