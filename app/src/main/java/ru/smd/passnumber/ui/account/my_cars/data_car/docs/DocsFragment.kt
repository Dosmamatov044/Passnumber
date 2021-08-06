package ru.smd.passnumber.ui.account.my_cars.data_car.docs

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.smd.passnumber.data.core.Constants
import ru.smd.passnumber.data.entities.Docs
import ru.smd.passnumber.databinding.FragmentDocsBinding
import ru.smd.passnumber.ui.account.my_cars.data_car.docs.adapters.DocsAdapter
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DocsFragment : Fragment(), DocsContract.View, DocsAdapter.OnClickListner {

    @Inject
    lateinit var presenter: DocsContract.Presenter

    lateinit var binding: FragmentDocsBinding

    lateinit var adapterSts: DocsAdapter
    lateinit var adapterPts: DocsAdapter
    lateinit var adapterDk: DocsAdapter
    lateinit var adapterDriverCard: DocsAdapter
    lateinit var adapterPassport: DocsAdapter
    lateinit var adapterCardCompany: DocsAdapter
    lateinit var adapterContractCredit: DocsAdapter
    lateinit var adapterContractCarriage: DocsAdapter

    var type=0
    private lateinit var imageUri: Uri

    private var mCurrentPhotoPath: String? = null

    lateinit var file: File


    var idVehicle: Int = 0
    override fun onStart() {
        super.onStart()
        presenter.onStart(this)
        presenter.getDocs(idVehicle)
        presenter.storeId(idVehicle)
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
        adapterSts.type=1
        adapterSts.setData(docs)
    }

    override fun showDocsPts(docs: List<Docs>) {
        binding.recycleDocsPts.adapter = adapterPts
        adapterPts.type=2
        adapterPts.setData(docs)
    }

    override fun showDocsDk(docs: List<Docs>) {
        binding.recycleDocsDk.adapter = adapterDk
        adapterDk.type=3
        adapterDk.setData(docs)
    }

    override fun showDocsDriverCard(docs: List<Docs>) {
        binding.recycleDocsDriverCard.adapter = adapterDriverCard
        adapterDriverCard.type=4
        adapterDriverCard.setData(docs)
    }

    override fun showDocsPassport(docs: List<Docs>) {
        binding.recycleDocsPassport.adapter = adapterPassport
        adapterPassport.type=5
        adapterPassport.setData(docs)
    }

    override fun showDocsCardCompany(docs: List<Docs>) {
        binding.recycleDocsCardCompany.adapter = adapterCardCompany
        adapterCardCompany.type=6
        adapterCardCompany.setData(docs)
    }

    override fun showDocsContractCredit(docs: List<Docs>) {
        binding.recycleDocsContractCredit.adapter = adapterContractCredit
        adapterContractCredit.type=7
        adapterContractCredit.setData(docs)
    }

    override fun showDocsContractCarriage(docs: List<Docs>) {
        adapterContractCarriage.type=8
        binding.recycleDocsContractCarriage.adapter = adapterContractCarriage
        adapterContractCarriage.setData(docs)
    }

    override fun camera() {
        if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                requireActivity().checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || requireActivity().checkSelfPermission(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED || requireActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
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
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                file = File.createTempFile(
                    UUID.randomUUID().toString(),
                    ".png",
                    requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ).apply {
                    mCurrentPhotoPath = absolutePath
                }
                try {
                    //todo some crash here after permission granted
                    imageUri = FileProvider.getUriForFile(
                        requireActivity(),
                        "com.example.android.passnumber",
                        file
                    )
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                } catch (e: Exception) {
                    Log.e("TTT", e.toString())
                }
                startActivityForResult(cameraIntent, Constants.CAMERA_REQUEST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
            if(bitmap!=null)
                presenter.addPhoto(bitmap, file,requireContext())
            else
                presenter.addPhoto(data?.getExtras()?.get("data") as Bitmap,file,requireContext())
        }
    }
    override fun onClickAdd(type: Int) {
        presenter.storeType(type)
        camera()
    }

    override fun onClickDelete() {
        TODO("Not yet implemented")
    }

    companion object {
        fun create(id: Int) = DocsFragment().apply {
            arguments = Bundle().apply {
                putInt("vehicle_id", id)
            }
        }
    }

}