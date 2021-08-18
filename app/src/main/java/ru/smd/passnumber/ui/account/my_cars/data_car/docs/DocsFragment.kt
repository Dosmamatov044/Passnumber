package ru.smd.passnumber.ui.account.my_cars.data_car.docs

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.smd.passnumber.R
import ru.smd.passnumber.data.core.Constants
import ru.smd.passnumber.data.entities.Docs
import ru.smd.passnumber.databinding.FragmentDocsBinding
import ru.smd.passnumber.ui.account.my_cars.data_car.docs.adapters.DocsAdapter
import ru.smd.passnumber.ui.account.registration.RegistrationFragment
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class DocsFragment : Fragment(), DocsContract.View, DocsAdapter.OnClickListner {

    @Inject
    lateinit var presenter: DocsContract.Presenter

    //todo set xml id without underScope ex:  use txtTitlePts instead txt_title_pts
    lateinit var binding: FragmentDocsBinding

    lateinit var adapterSts: DocsAdapter
    lateinit var adapterPts: DocsAdapter
    lateinit var adapterDk: DocsAdapter
    lateinit var adapterDriverCard: DocsAdapter
    lateinit var adapterPassport: DocsAdapter
    lateinit var adapterCardCompany: DocsAdapter
    lateinit var adapterContractCredit: DocsAdapter
    lateinit var adapterContractCarriage: DocsAdapter

    var type = 0
    private lateinit var imageUri: Uri

    private var mCurrentPhotoPath: String? = null

    lateinit var file: File


    var idVehicle: Int = 0

    override fun onStart() {
        super.onStart()
        presenter.storeId(idVehicle)
        presenter.onStart(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterSts = DocsAdapter(this)
        adapterPts = DocsAdapter(this)
        adapterDk = DocsAdapter(this)
        adapterDriverCard = DocsAdapter(this)
        adapterPassport = DocsAdapter(this)
        adapterCardCompany = DocsAdapter(this)
        adapterContractCredit = DocsAdapter(this)
        adapterContractCarriage = DocsAdapter(this)
        idVehicle = arguments?.getInt("vehicle_id")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDocsBinding.inflate(inflater).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            btnBackDocs.setOnClickListener { presenter.onClickBack() }
        }
    }


    override fun toBack() {
        requireActivity().onBackPressed()
    }

    override fun showDocsSts(docs: List<Docs>) {
        binding.recycleDocsSts.adapter = adapterSts
        adapterSts.type = 1
        adapterSts.setData(docs)
    }

    override fun showDocsPts(docs: List<Docs>) {
        binding.recycleDocsPts.adapter = adapterPts
        adapterPts.type = 2
        adapterPts.setData(docs)
    }

    override fun showDocsDk(docs: List<Docs>) {
        binding.recycleDocsDk.adapter = adapterDk
        adapterDk.type = 3
        adapterDk.setData(docs)
    }

    override fun showDocsDriverCard(docs: List<Docs>) {
        binding.recycleDocsDriverCard.adapter = adapterDriverCard
        adapterDriverCard.type = 4
        adapterDriverCard.setData(docs)
    }

    override fun showDocsPassport(docs: List<Docs>) {
        binding.recycleDocsPassport.adapter = adapterPassport
        adapterPassport.type = 5
        adapterPassport.setData(docs)
    }

    override fun showDocsCardCompany(docs: List<Docs>) {
        binding.recycleDocsCardCompany.adapter = adapterCardCompany
        adapterCardCompany.type = 6
        adapterCardCompany.setData(docs)
    }

    override fun showDocsContractCredit(docs: List<Docs>) {
        binding.recycleDocsContractCredit.adapter = adapterContractCredit
        adapterContractCredit.type = 7
        adapterContractCredit.setData(docs)
    }

    override fun showDocsContractCarriage(docs: List<Docs>) {
        adapterContractCarriage.type = 8
        binding.recycleDocsContractCarriage.adapter = adapterContractCarriage
        adapterContractCarriage.setData(docs)
    }

    override fun camera() {
        if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                requireActivity().checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || requireActivity().checkSelfPermission(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || requireActivity().checkSelfPermission(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                Constants.CAMERA_PERMISSION_CODE
            )
        } else {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            file = File.createTempFile(
                UUID.randomUUID().toString(),
                ".png",
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ).apply {
                mCurrentPhotoPath = absolutePath
            }
            imageUri = FileProvider.getUriForFile(
                requireActivity(),
                "com.example.android.passnumber",
                file
            )
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(cameraIntent, Constants.CAMERA_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentChooser()
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                presenter.context=requireActivity()
                presenter.file=DocumentUtils.getFile(requireActivity(), data.data!!)
            } else {
                val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
                if (bitmap != null)
                    presenter.context = requireContext()
                presenter.file = file
            }
        }
    }



    override fun onClickAdd(type: Int) {
        presenter.storeType(type)
        if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                requireActivity().checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || requireActivity().checkSelfPermission(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || requireActivity().checkSelfPermission(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                Constants.CAMERA_PERMISSION_CODE
            )
        }else{
            startActivityForResult(intentChooser(), Constants.CAMERA_REQUEST)
        }
    }

    override fun onClickOpen(url: String?, type: DocsAdapter.TypeOpen) {
        //просто открываем по url через Intent
        when(type){
            DocsAdapter.TypeOpen.Photo -> {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.setDataAndType(Uri.parse(url), "image/*")
                activity?.startActivity(intent)
            }
            DocsAdapter.TypeOpen.PDF -> {
                val googleDocs = "https://docs.google.com/viewer?url="
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.setData(Uri.parse(googleDocs + url))
                activity?.startActivity(intent)
            }
            DocsAdapter.TypeOpen.DOC -> {
            //открываем документ (doc, docx, excel и прочее)
                val googleDocs = "https://docs.google.com/viewer?url="
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.setData(Uri.parse(googleDocs + url))
                activity?.startActivity(intent)
            }

        }


    }

    fun intentChooser(): Intent? {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            file = File.createTempFile(
                UUID.randomUUID().toString(),
                ".png",
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ).apply {
                mCurrentPhotoPath = absolutePath
            }
            imageUri = FileProvider.getUriForFile(
                requireActivity(),
                "com.example.android.passnumber",
                file
            )
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            val docsIntent = Intent(Intent.ACTION_GET_CONTENT)
            docsIntent.setType("file/*")
            var listIntents = mutableListOf<Intent>()
            listIntents = addIntentsToList(context, listIntents, cameraIntent).toMutableList()
            listIntents = addIntentsToList(context, listIntents, docsIntent).toMutableList()
            listIntents = addIntentsToList(context, listIntents, galleryIntent).toMutableList()
            var chooserIntent: Intent? = null
            if (listIntents.size > 0) {
                chooserIntent = Intent.createChooser(
                    listIntents.removeAt(listIntents.size - 1),
                    ""
                )
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, listIntents.toTypedArray())
            }

            return chooserIntent
    }

    private fun addIntentsToList(
        context: Context?,
        list: MutableList<Intent>,
        intent: Intent
    ): List<Intent> {
        val resInfo: List<ResolveInfo> =
            context?.getPackageManager()!!.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
        }
        return list
    }

    override fun onClickDelete(idDoc: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Вы уверены, что хотите удалить документ?")
        builder.setPositiveButton("Да") { dialog, id ->
            presenter.deleteDoc(idDoc)
        }
        builder.setNegativeButton("Нет"){dialog,id->
        }
        builder.show()
    }
    object DocumentUtils {
        fun getFile(mContext: Context, documentUri: Uri): File {
            val inputStream = mContext.contentResolver?.openInputStream(documentUri)
            var file =  File("")
            inputStream.use { input ->
                if (documentUri.path.toString().contains(".jpg")||documentUri.path.toString().contains(".png")){
                    file = File(mContext.cacheDir, System.currentTimeMillis().toString() + documentUri.lastPathSegment?.subSequence(
                        documentUri.lastPathSegment!!.length-4,documentUri.lastPathSegment!!.length))
                }else{
                    file = File(mContext.cacheDir, System.currentTimeMillis().toString() + documentUri.lastPathSegment)
                }
                FileOutputStream(file).use { output ->
                    val buffer =
                        ByteArray(4 * 1024) // or other buffer size
                    var read: Int = -1
                    while (input?.read(buffer).also {
                            if (it != null) {
                                read = it
                            }
                        } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
            }
            return file
        }
    }
    companion object {
        fun create(id: Int) = DocsFragment().apply {
            arguments = Bundle().apply {
                putInt("vehicle_id", id)
            }
        }
    }

}