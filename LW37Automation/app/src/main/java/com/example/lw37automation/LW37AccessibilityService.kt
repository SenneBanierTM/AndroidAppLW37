import android.accessibilityservice.AccessibilityService
import android.content.SharedPreferences
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.util.Log

class LW37AccessibilityService : AccessibilityService() {

    private val roles = listOf(
        "Military Commander", "Administrative Commander", "Secretary of Strategy",
        "Secretary of Security", "Secretary of Development", "Secretary of Science", "Secretary of Interior"
    )
    private val acceptedTags = mutableSetOf<String>()

    override fun onServiceConnected() {
        super.onServiceConnected()
        val sharedPreferences = getSharedPreferences("LW37Automation", MODE_PRIVATE)
        acceptedTags.addAll(sharedPreferences.getStringSet("tags", emptySet())!!)
        Log.i("LW37Automation", "Loaded accepted tags: $acceptedTags")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        val rootNode = rootInActiveWindow ?: return
        processRoles(rootNode)
    }

    private fun processRoles(rootNode: AccessibilityNodeInfo) {
        for (role in roles) {
            val roleButton = rootNode.findAccessibilityNodeInfosByText(role).firstOrNull()
            if (roleButton != null) {
                roleButton.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                openListAndProcess(rootInActiveWindow)
                navigateBack()
            }
        }
    }

    private fun openListAndProcess(rootNode: AccessibilityNodeInfo?) {
        rootNode ?: return
        val listButton = rootNode.findAccessibilityNodeInfosByText("List").firstOrNull()
        listButton?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        processApplicants(rootInActiveWindow)
    }

    private fun processApplicants(rootNode: AccessibilityNodeInfo?) {
        rootNode ?: return

        val applicantNodes = rootNode.findAccessibilityNodeInfosByText("Applicant")
        for (applicantNode in applicantNodes) {
            val nameWithTags = applicantNode.text?.toString() ?: continue
            val tags = extractTags(nameWithTags)

            if (tags.any { acceptedTags.contains(it) }) {
                applicantNode.parent?.findAccessibilityNodeInfosByText("Accept")?.firstOrNull()
                    ?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            } else {
                applicantNode.parent?.findAccessibilityNodeInfosByText("Deny")?.firstOrNull()
                    ?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }
    }

    private fun extractTags(nameWithTags: String): List<String> {
        val regex = """\[(.*?)\]""".toRegex()
        return regex.findAll(nameWithTags).map { it.groupValues[1] }.toList()
    }

    private fun navigateBack() {
        performGlobalAction(GLOBAL_ACTION_BACK)
    }

    override fun onInterrupt() {
        Log.e("LW37Automation", "Service interrupted.")
    }
}
