package com.app.app_demo.utils

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import com.app.app_demo.models.ContactInfo
import com.app.app_demo.utils.AppConstants.Companion.USER_TYPE_CONNECTION
import com.app.app_demo.utils.AppConstants.Companion.USER_TYPE_INVITE

class ContactUtils {

    companion object {

        fun getContacts(context: Context): MutableList<ContactInfo> {
            val contacts: MutableList<ContactInfo> = mutableListOf()
            val resolver: ContentResolver = context.contentResolver;
            val cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null,
                null)

            if (cursor != null) {
                if (cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                        val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val photoUrl = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                        val phoneNumber = (cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()

                        if (phoneNumber > 0) {
                            val cursorPhone = context.contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                                if (cursorPhone != null) {
                                if(cursorPhone.count > 0) {
                                    val list: ArrayList<String> = ArrayList()
                                    while (cursorPhone.moveToNext()) {
                                        val phoneNumValue = cursorPhone.getString(
                                            cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                        list.add(phoneNumValue.toString())
                                    }
                                    val contactInfo = ContactInfo(
                                        name,
                                        list.toSet(),
                                        "photoUrl",
                                        "",
                                        USER_TYPE_INVITE)
                                    contacts.add(contactInfo)
                                }
                            }
                            cursorPhone?.close()
                        }
                    }
                } else {
                    return mutableListOf()
                }
            }
            cursor?.close()
            val empty2: MutableList<ContactInfo> = mutableListOf()

            for (x in 0..499){
                val list: ArrayList<String> = ArrayList()
                list.add("23194$x")
                empty2.add(ContactInfo("abc", list.toSet(),
                    "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxITEhUTExMVFhUXGR0bGBgYGBgYFxkYHhoXGBgYGhgdHiggGh0lGxsYITEiJSkrLi4uGh8zODMsNygtLisBCgoKDg0OGxAQGy0lHyUtLS0tLS0tLS0vLS0tLS0tLS0tLS0tNS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLf/AABEIAKYBLwMBIgACEQEDEQH/xAAcAAACAgMBAQAAAAAAAAAAAAAEBQMGAAECBwj/xAA5EAABAgQEBAQFAwMEAwEAAAABAhEAAyExBAUSQSJRYXEGgZGhEzKx0fBCweEUI/EHUmJyFRbCQ//EABkBAQEBAQEBAAAAAAAAAAAAAAABAgMEBf/EACYRAAICAgIBBAMAAwAAAAAAAAABAhEDIRIxUQQTIkEUMmGR0fD/2gAMAwEAAhEDEQA/AOsuOhY0lun7RYsNjSe8V+SRqsSfb1h1IUlnJ8oyDpDqXqLgD3h9LnAJcRXsRjEhMAS84UEFz26RAN8fipa3BMIkT0ILp57wjXmqgtwzE1p7QwxoaXqe7Ewolluws5kirmBcditJ4jSEeUZkhMj4iiXL05MYR5xnTqBdx9IlFst65EuamwdTP1EDKyPgLNSoiqYfP/goTMXUvRAIch79KRYPDviZGISrUyFpPyivCbH9j/MShYArDKkrIqdXpGxjVApCgQ/p5x3N8VYZc1CQx1agVbOCQgddTE+aesLsVjETZjS1cQLM3d/pChY1Cil2NTZo7keI8RLSUhZYWcAntXaEyFq1M7hnBjlU06QokHoTf0iULPUvDuZqGHkHEL1LmKIBYAi7am9HYXHeLCBHjfhvM1/GliZMUJaS7E6gm54Qbbdb1j1fA5lLUkqChTV3YEh25QKGmXGtMA5pncqRLEyYrhJAHnv5CvlEWE8QYebMVLQtykAvTSQQDQ73+sCjTVEK0GFWJ8TYdBmAqdUu4FyXZhzLx3g/EElaX1JF6E7B6+giAaylEUrEsuKGrxx/dbSPhlbP/wAXbV+8AZz/AKkKk4haJaETEAgAks7PqLjmSPTrSoHqqTGlq6x52P8AUtJROWEOUhHwkG6lEHW5B2h5/wC34ZaFrQqiA7HhKjyAPkItgsTl4gxLAVgLLs0RNlpmJsoenSOcZigReJYOAmr7RiUbwIjGgUKmgrD41Cw6C4tGQTCJQnpEshKYnKxZo0kUFBbnG9bQSUDeIFJBNooOQuNkAxKjDjlBKZMKAuVLEa/pusMxKiRMrtFoliReWg7xCrJhtFkCOkZoEOIs8nkpAW3tEU19amBCQPeEOKzb+4gJJFanpFsRKCwkncOesbMFfzDMtAFi9+kK8VPVpJdhGs+S0xTBxuRZ4S47FFQSkU5wRBlMGlIVz+sdTca+mtGqLwuXjeAJP4YXyJpYgC9oAZ4jNXToAIS9XLQnmYl3vQ7wdMwxUlIAdr94GVh/5gACfOJLmO8NilSyopNw3rEk7BGvIb9IHXhVJd4ugckEnzgiVNUghSbiIpUomv0iadJ0ipEGBlhMyCRVqhvRz5RPi8cpTFLAmzWA6QgoRu70juXMWjpyiUC75fI1M7BZena/+If4aTMAfb9vtHncnGzNYKXC2oXsecNZnimajDiQGcOCqpJSQQz7M+3IRhoqZeMfJVNSlKlEhNgbDtCg5aUPwsD6xB4b8YJWUSpzJVpYrP6lUbZg4c1N2i3Kw4UKkxmqLZXMPlTh9zc9Yim5ctJvQcoe6CikAKUpRJJpy5wAqxMsFBYMWvFWxeDJLG46R6Bl8gEKcODHGNyVKUFQDk87RU6BRcNJU7gxrGYxQU8MpuGehATyaF2ZYIOxJp0+saIOMu8Qz9Hw0KIS7huf1ixZBil6FJUd37UaKfk+BK6JeLdk8jQ6SXjDSNWQ51jFjnEeW53MkFCg5S4Kh039ngnOpJJABvbrC+fhtIDOWo3WFCy0D/UA6FAywJmnhUA4CmXUh610U7w18M+NJM2XLTiJiEzzQsCATqUE9Elkg8qjm0U9GVakAlNaQtzWRLkpNAVt8o+ZufQdYCz2uVPlrQlYWClQGk7F7erxLKCCwCgexGxY+9I8ll5pMVgpcuU5CTqSobEEEDoQXML8mxOLlTZU3iIlEhi7aFElQ8yYpT2HOM6w+ECVT5gQFK0jerE2FWYGsNAI8I8S4vEYnUZhJQFEpBfhGpRtazegi3+Hf9RpMvCy0zyfiJUmUwFSjhHxS9AAkuf+pYRpMHpIEbiCdikoQZhPClJUSK0AekI/D/jTC4qSZoUZekErStgUsCo1seEO4jWiFjjIgwuLlzE6pakqS5DguHBYjuDE8Uh8xZbmCFFzccUWPJ/FyVghQ0t8tb+fOPNxMIc+UaRMKTeFGT0LEYmVNEwIIcEuOv794pi1KCi8c4XMFBeo+fON4yc9WZ4AkUgqg3AyfmNB1hZg8UQeYIZusFqxRZm8vKAJsRiwmgNOkbwuIfha8By5SlBzaHGRZWZgKwS4oKUMAFYfAEgkp2N7Wis4kEKKT9Xi9mUzVNq/aEn/AIxK5hSxcRmwJ8uwqiKGh6WiLMcOoAEgtat4sc+ecPI0jS/INz+Y+Qiv5lmKpzaz5xU2AAhqjaJkqJpekQrUGp+WjJcxiaxWCWXPIIMSypzkn62heVx2lXDFaAbKQmtS+xf3iw5Z4txMrWArXqtqBVoLglQcuXDhidwdq1WUtm6i/WCcPNIWD+NyjDQPV8xz+ScNLn0UVKSlaUmqFEOp+QABNbxFOSCgEWUxB5g1HtFFOD0gAVCqv9RDyUqXOly5SAUGWATVgVU1K+0ZaLZaMtlQ3xcoKRp6QkwU8S3+IWBZu9Aw84ZScSDxAuk2MQolzXLNCQpKSWvCfDpQs/3aalMHG8XxOKlqUZeoFTPperc2imeLk6FoTYVNGa8CFhynIpcoOkVMB45ATODHcUgnKc8Cpen/AGoHFzLQlxGMQFpClgEncwoWTeIJw1ywBxJIND1hHnmZlGJSEkEJY8739jCzPM9UVlSNJBNCf9ooPWFOKzErVrarAegaNJCz0TH5wJmFGg/DUpYA3cJ4j22isZlmPxMQtRIBXdWwDW7bRW1T1FQLt3+0SDEHcvChZafDGfJkOFJPwiFEu1VBtDb1AHSvSLNkfiWVOVLkrQy1vVNUu/CALsRvz9vLVzaEA0g7DZktIQoKIUkEAvXyPaDQTPXcXhZenSopGqiXI4ixNOdIpmL8MJ1BRmJCSUgHmSRpHm49YSzs/CsNKQQVTZbh120OSADubCGOGx8soCDMcqUlQfZXJ+d4lUWxl4hk4lEooE2YtDJTo1KZhRNNwB9BFRlCdLdiRqBBbahTcVBYmL5PzKXNniSmaHPJyXZzW3lS0TT8mly0KU6VPUP7eUWLoMT+DPEk3BqQCpRkqUStJa5DOmju4e+8LMd42xaxOlCbN0TFApOspUkJJIYj5Xo4dqbwAsBUwhbkMSD1q1NoSqDlhzo5jSJYvBcRqamojqYz0qOcYpCr9I0Q0zWjtaqHf8vGaD/tPoY5QkvAGpa2Y/jwWmdxd4Ddj+dIlWHIIiMFpyplpAI6dIumUyEIQEppv+dI8yy3MzKIHUX5bxYci8RKmL+GtklRIB9GEZYLh/ShS3c2J6ekblYZAUWDqJcnl2hHi/E8lJWjV8ukAgHUT+q+1okyHxdJWk62QpOonkwLBzzIIiFIfF2UukEfKB52/PWPO9y/aPSs2xyJ8p0qcEAtuLFveKbmWWKQoqZ0tWn40VEEKiXjTx0tFfpGlJjoDkxIiOVJjqWIMHXWJUTmLxx8Lhd/1e0anSylVfzrEBbMFmIKUpPL2p7xBicXoWVS+J3d3A9IRYZZdIiVU2vYl4y4gsq83UJEspT8pDvtuEhuYesPsnxSZ4SywFboBbS70bqz+Ueb/ENnNfSJcNiVy6oUQTyLRmgXTGoVh8UJodTAu9PftA2LzRU9H925G0cZL4gliR8OYklQoLMQTzJfr6xHOllaQpCS3QGkVIEeKmql4c6VkKJABF2fntR4UZhOVNSFE1FDBs5CVpDKCgFVbYgQBOfQa7xoC0F6HaOwKdvWNqktGparvFoGkVBrGJSLxtDOY5BiA7CWF/znGgsmOD9I7TSFA6MytoxK3jSJvOJUAVPSFAmwy/lO4LwbKzZUtZIUSkhlJdxp7HlX1MKtiBEuFBUD+PChY/z/AAstCdUtZIUAbgkDkWhTgknUKUr1BjcyQsBmOmj8qUgvLsZ8KcNaXS1UgDkWI6vBAr61h/rHAmG8FTMtU7iqBV39mgU2aCafQaoM+IQzl+cD4icFMws9fpGIDprHBlUHX8H51hQOMKpOriDj943MZ2FhEy0JsEs1ju/WsaRId29/vB12CFYPeNsUqBB67XFYkkIc1cMdg5iVUgsSC/16UicknQB1zSSSbm/XvGgOVo7WkpLVeNAPtFAZl+YKlqoaMe1Q32g2d4gmKCk0Cbgcmenv7QnXSg3jlrRKBOZ/FYX9o4SgKZy1fbcxwkRiooNhDdY7l4eo7xzhVsXNRDnALSWoLlz0Yn7QsgJ/QqUQAk3g3PcvCVBA+alehvFwwGHQnRpSFMrlYkfaGee5ECApIAffduUSweYoy9k6nqIhSaVoY9C/9fGgECm/8RWM1ytCSW5xqwVpY4o60kQ5VhUlBU1U/WFykE/paAshCtnbtyg5WaTBLVLExTagbnbYVoID+ETxMQHaIlByYULJsPOZvem0GSyn9RptG8DgiUqUwu31iHEyCksYJCzibeBm9zBRQSl4FRFolmzLYH8eMKKtWMJ9Yya4VzpCgdLkvXaMWhmBjlCncGnu0TtwioPa/wDEKFkEpHFEgqaRtKGr9hG8P8zxaFnRksa2jZFRtBGKDEXs8QE1c7RAOsICo3PnBisIAQSBTdRbnzMLsvxaQyjuQxPdoI8X40qUiWlTJQOJt1H+P3gLEspRSA/Vjdvv/IjjE4cUYUNq8tzEs1KkHTtS+9jEsiaVH4YSSDsL2P1jzRbTs6sC+G4ptA6fmtvB6uAKcFJs1adKxyJSmfSehIsY7o5gU41BoOjRqZKUkgqFDVn268oPOFCSSsMnkWfnvUQMJyC7ihLA7W+nWMuX0UhK1eTUHL1jXxlPfa8dzlpo1ohEwbtGaKdTVOASz7mMkp3amx/xE8mWkmj3N69aH19I7mTKnp69o6J60QFVZjGiqO5ssnYvuGt3jhMonY7+14pDTxuUHMSmTQULtbr++0coQEuHHpUecAam07RLJUw+vKNrRw2qeUSjC8Ne5AiAOyrPpslZIrY18g/pSLj4Zz6ZiitMw8SVFSRtppw3qBHm03mz8/vDzw7iTJmJWPmrzseQg0D0fE5mlfAKO49uQir5hhzr0fq6hoYYfFcYOkkmrgWINT+3nDrGyhqTNIYgglxyp5Rnko9sU2UtGVr0KWE0BZXfaJMtkmYWKEsksSWd9/rFpx+ZyU8LEargD1pu13irY3HISr+2rSCTU3NnJG8eefqk9Q2zax+SfO8skkOFhkgcI+r9oRSMvkghRJUCTagZqDqHasT47EKWVaSxLVFm23bbzgRSVmgehuw4rk/SOayZJds3xSGEnNUgFPw0i4sG8oll4eTNQdQU5+U2BPa/TzhemUwS7VazvdmJ9IIMvSXIPTts0Fyj+rZaT7IzloSniodgP/rpCs4FbOzfvDqSEsSSX61J5/naGWW4ITAU3SD/AAY9ePJf7M5TjrRUzl6gAW/OUQ4jCLSpiGLVfbnHpYy2UhDKZQ38t/eE2PyzUvUkhbhnesb92F1ZjhLwUuWHJsCKUDecOcgysTH1OE7K+/SG2U+HyVlkt3/mLFJ8PpSBxEkEuBY943aM7KtPwMmVO+GX77WBPrZoGGEQtbgBId+VIZZvhwuZxuG7e/oIDmqSniT2H53eJZQVeCK1kl9NhAs7Bh2D/m/vDCbOUoAC43+rRmXyFTCrUCSBbev+SYl2BROFgSyR9qfR40K7Of2h5mGVKWSEpNNvy3aK9rKC7VtFotlh/pZZsNRrvQ9tuV4KwOWqTMC0jTcEgip5DvAi8CpMzhClJNEkMSDyUNmp6w4wuVziXVMShLXJBG1E7x8vJOl2emnfQYsS1N8QAtd20u7lxevnESJSNtQFn9LUblYROvItkzk1IFSAA/k52pE/9CAnTqCt3Sqzf8TaOEcy+mbor+ZZPL+GpQ4lCgI0g1NTSr/jRQZzai3lSPV5kuWA4GoEWNiK/hjnDZfh/iFaZICjZSnIFNTAGgTSPQvVcO1ZiWO2eXSsMaOQBW7flxB2CydeIUyQEgC5sz0sOf1Eejz/AA5hZiwualQZ3APColjV6g0NQ0EyckkUXJmJlAONP6SKUAehoziMv18fBFiZTcB4Z0LZagoCwSPm3ufpDCZkkhytSQRydkAtvzAH7Q6xGDmOQSNnYMm4oD+fQRLMwSiggrAFkhied2g87auzagvAmwwRNckAdUirAOHINTsHG0TnJZRAStKykmoB0gVO1w55Ex3hslKCn4RB1Bi5IpQEvff2jr+mW/IC6gDVqUHXl1vGea+mK8nUrI8Kk6vgAqJDuCsAuADen4TAGa+G5KnCGlrJ2StSSeT339oazZ+kqSkqcbUv6+/8wMcQV/Ma7VHs9PpbrGIzn2pMjS8CAeE5hWNC3DVJoQRfhb5eR7RzifDk4JcAKPdIYedPwRZcEheopBSpns9ObsKV5XiSblQKtapnCNknoWIPfbpeO35U090T219HnKcvVq0kHWSAEFqvXe/eLVIy9EgJOlMxe5uBYEDoPKLAcDKYL0grSGQVhJI5sKVLX6mAZ0gI1F2JqR3FQKc/wRMnqXkpLRFj4kc7NTJTpSGP6RpejBvIQrxebzFoLrKuwa3Y1NxA+NnEEquCWc197wFh8Ks9BZyQPrfyrFx4Yr5PsrkyT+qWSFBqUrTYiBZ6QVHVa+x7t1gxGXcOosXHCARYXUWen2tEuiWmXqSlXEwJUQ3NVGDVo/SN6XRkDRPSCwSyQxZ3839YlRiWNE0NjvaMIUVUSztdn23NYgxEx2DBnb8fvG0rBLt1cem7PtE0/UwJLim/38oX45agAxF3+0blTlNxJ07gtRqc7xa+xYylsxZId97WEF4HEKSXSdNnHP8AP3hXInHTVyOTsxt5/nl2mfcU+tvrCrKmPfiqcuos29exjFzE1Zhbie9qs+72ELsPPcEFi9BWtKwFjFFJABp0q1nc945e3vwa5Fpw2LWlgCFbuzParOYf5NjUlxMsRc/SKBgMatmBc1+j926QXKzBRLue35btHRco9E+L7LFnWTLmznSGTWpFA2z/ALQj/wDAT6y9NXYHZq8TnaGWH8RJAAXrOzA07t+Wgo5slgAVW/3V++5ifkZb6Rn2o+RZgPD60JIXc0cEME9zzrbpD/DZeqUEslLJHzfMS70Z7dYRjN1OyiVgFr/nKCVY1EwnjUFHc1A6O0ZefN/DSxwGapiVFZBB1bCgTYXdhSKlP8OutlzAmWHIUgOroGLCvPpzh5hMvqSZiSTyFPKt/wA79ryuaU6Zagp72S4vUkttzvGZepkl3Q9teCvYFcwktb8dm5dbVhhPWlQSFcQTyO/lesIMMTLSQVAvsCVMdvb9oOw88E6jatujVAMacU2VMmOarD7EWAHoAP3iORmM1wtjoFaCj3BJ5kxOpEvqC901YOHA69eoiZWZAMJZXoAA0qZQpRjyttHOVLSiCbD4n43GyAonhcqIKn5VDPsKRNMxa5adLOQ5ob1IDbjdwOkAlDAgOkByyaBiR67bwOoahpLC5Sp2FuJw1i+3KOLim/4WyLHY5baSo1N2vV2PvE+DlTlJo7E36ULPs1/WEE+eQ6C6WPQ9DXfeGOVz5iW0lA1UBUTffsAOd3j0ShUdGE9lxTN0gIcqowYksw9LtEczFlLsaWU7PX+D7QimYsjVxAlmcEdi4Y0c0rEC8UdJClBjcfqfkzDZvWOUcWjfIsKs24eElqgH0JMbk58wJABVY0v582eKrOmkUcAMWYu1uW+3rEeKzDhbWph+c4r9OmqJzZZZuMlqZQBNzUgFJNw3ne0BHCKW5SUiwZ2p/g+0JsDigX5gU69zyDflYe5XjgSKBu21YvDgtBO+yfAYeYngQCSWJVQFhs3Y7n6R3NmrCXLsCw2B7v8Am8HKxKZZUoGofT81H6WLAn0MK8ROOmlXpUMQ5Ad9xYejxmK5bZro4GLUb1FRyBBoz/l43i5RDuBRrhq97nvEmUIClEkuzOAQQ/I26A9jsHgvFT/iHQh6i6hV7khNNrPyjTVPQW0VfOSQyiA4DlqFhsAQGL9r1gPB1WNRD8+Qqatc1v8A4i1ZngUhDTGZuGxNqbkE3FD5xW8nlKClq0pQQksSRQhSW8qnavvHaEk4tGJKmESgkDWl0kgggsFbUAFWq47mF+bqUCwB0iwsGpcWf8eGYIC0JDKUVaVqBTwukBJ0gAFq7lg0K8wAUVEkMCNzuFEgXa49o3FbsywWqgW1Pd37fSJVsjQClyS3PsWjeHwswkgJVQEltgwLPsbsIgxOFmJKdYN6cyH6VFfpGtENLmjWDSjXqCXO3SC8TOJSQeIB6BISNuV/5gBKElQdKrgtzu21qwVjMSSDya1KH8aDSbQNT8RqBA+YUblv+wgdWI0lgY1KXVRIYuBQM/M0iGWCtVBz2BYV/iNpJAY4TF8VLippQgVNq7QVOzJm1BKnO4uT0EL5SNJ1EjkGDnlbz5xpGMVZAqdlBJG9y37xzcU+hY7wuKQoafggACqhqSa0BJJavWI14MpfSpBYPVQ7gA236wpxeIWWTqJAsBYnsmkN5WT4j4WskKo4Rq42H/A1A7e0c38O3VmlbME6WlNfm3JJoe4NulabxK7Ci0noKvRuVqdbeiPFzSo19j+zwWFLIdP6mFBxE3Yegtb0jpx0SwlQCXJd2G+3NvasRyMYNtXR27XgzE5bMlyyVJ2Brtcjrfz7RXxMYlw9LVbf7CKkpIPQ+l5kUsSTzu1+XWGuDzYqOkJBJ5B1WerXMVKbOZ1NyblflvFp8PZ7LQgJXITqlk/3UnTMZT3JBq9Ox9eOeHxtRs1F7Kvg8KTOloJ0gLertoBDs7Evb9ouJkYJRUUy1JLBgCdBNh0Fvr0iuTsIpMz/AKij17/uIFl4pYuqidjy2B9oO5bTC0XWRhJYlkBIUSCH87s7pYV8oXS5Alk6SD003fhcGuz+kJsNj1K4nI+24Zuw7QZlmJd0rsbfV+Qbrz9eXCS7Zq0FpUVGw9nI8t/uIDxcol6WFBYk2AHKkc/1CUkp22IZ+pB5Vv06RF/UDUku79d7b+fONqLI2Byslnq4gHd3FC16kXg3D4VaEqSSzgF9JpWlfywhjhZiD+k3ajUNQfy8Ga5CSVqAWovRZUQBVrdOfKMzyy6aM0VhBYFT17DnvzhnIwhUEqLOQ7/qYpLlgeZFTyg2RKwqnWlLEPwhZICnFWVf6RNiESllkqUVHcK+b5aEUY9usSWf6SFFazCSlMpysggBw1jyf8tAWDxWmm53O38/aLuvKJEwKBIIUzhJFW5g7PvAeK8NSpcwKly9blwCXZg5BSaEeXKLD1MGqYcWIMGqWC5dRdwBt5bw/wAOiYqWFS0gBhw6uIVs17bRDjc4VL1JMtLkAl0izUFns8Dozda1hJeWFF3qWsnUz8wRCXKW6KtFrRhdKePSV/K6VNTZLDfYRiUISjQkAGtya2a/YFmvzivz501Ck6ZqTwkk+ZAoakkNEWBxsxQ4lEpfsCaHa28eeKktpm7Q9w6Phgh0jUQVaqVq1fN4m0AB1GlxegFWHqH7Meq5SpiwUhLCjKZVn5tf7xEwSkOs3BVXYVHY/eN8r77HQROSluFRUz0mHV3enJx9YVJw6UgmaVJQAzJ3H/bvyPnDjDzZaks1eZ3Luo12o/eO5okqI0hJa4CqLDMkEc77CqerxlZnHTK0mV7J8HrmEqSlISOIJJYAt+o1JNa08obzcswx06kkEL1EnUQrYgjcDh9IJXipRSlLaDsD8rDYD19DyhaUqKGCgnYJLkq5Nyt+b7eRz23RmkibEzgs6HDEs5DaWLbVFrNvtHMzCS5QdROttJNxp2YDp+ViD4WJQAlTGlr8VwBuHFWpHWLUSE637g0rVIO3Uc4i8J6B2mTKSdRqSPmIrQ0SACGF/vEGMyWVMImhY0uBpYhxdTkVDl6gFnjidhVEatXJKaF6guTVqW84mlrmS9RI4QLFmfYgG1eUatr9XsjNycmwqErSkrUVKcKLOAxBqzsxPpCHC5Li1khMpSdRuHAqRXsL02h8qcnUklVCCVJtpP6aE1c8q184YTc2TLlsCvk7gnehs3KL704/2yUmVDE5LPlKPxkG2rY7p1Hk4BBP+YXzVivCoC+9d7GnP0i4YLPlFJQsFmYKaocMNJvt7dBEWBwmGnqTLKlqOnTrcAgaRUDkL1e8dY52v3X+CcfBW8pT8SchBUUg/qDahZr07nk8eh5dI0yZaV8KxVgHLv357fWEmG8MGRiErTN1ISS7gak1IHe4L0horFsqtVGnWzO3d/WOHqJqbqO0bgqK/m/hpRmTSVoR/tAsFKYpdvlHOn3g/BYIYYkAFbq4FFnLCtma7F+1WgiZjfmZ1EXWn9NwPdRvDPCyaomqS50DS9Q4u7hhXT9qRmWaajT6FKxYormoZKFEqoVPRqOH5bf4g2R4Xko0FQSvhKWUEqSNRB1AV9Otnhh/VKIIUSpO7JGkB6DT5wLJSFf3EzCA5oSAwDg+VC305Pe1o1QuPhsJA0olq6HU9iaalF7nz7GMOWISvSlHCAOFzpSWBIcF3fdzyjc9CVmj2FjVQpXZ6cutommYyYgAKFFOS44WDNbeOiytkdFTkqJDBbHvQ15tsG9YIxKRpS+lzQkXuCab2/iKqjFl0vT15QScf58ulG+0el4nZzTHsySkpCEMRRiPoSd35QHhZxEw6qBLOxFGZm72vAkvMWYNR35dr/tE82ZVw383DRFFrTKaxhXqJu7HrWjHqPtA6pqgHUKCg73bpSGK8vX82oDU5IOx2Ybn+Y6nSZawEkHUdyXrz6RVJEoOE9DhZUKjiTud/m+nOBMznJYkVQGpRw/W5rAEmRpcFdA9N25B+sC4mdtYG/Pl5mMLHsNh2ExyZZKpYKuRLAioIAbf6vDHDZikV4akupqpJOzXpt0iqf1BFHp6Xb88o4E9lMS77j7Rt4UzNlvwWaBCipQKVCjgtQk1a38wVJ8RMSkS3S/zAl3apDv6RVk5ikhlp1aejBqXb8vEKZykqJT8tN2d6sfvHN4E+0XkWrOHmhRAqQK2Z+dP+u7/AEgLATDLUn4j1o4NvWBEZiQpJf8ATWxHKx7xPlS0zidaiwIIYhzsCzNzHnE4uMafRbJsfmSFK0pLB6que37dKxn/AJaYiik8RICioB3cNT9ukGysDIw0xSyp0sfJ6JA7PeCJ+Kw890iho7iqgLfVR5veOdx1SteSkWC8QguhRZA383s+5L0gtGNlr6lYoW/TRjShqPpCDMcuSs/2iygGCDQqU48mFfXkBAKV6NKDr1iigSGo5J6Dv6xVijLcRbG+YYpQoAXSKtta5Hl7QLIxzKcqZxUl70P1+t4IwGdKQXKVKLB22fc9+UEYvL8OErCkq/uFwpLnQfm0kEs9Ghaj8ZIEWFzHW4+ZwSAzkFrDy3jvBbzJiiksyQfmfY9gK+bQJgcMMKr4hXqUKNRmofcQLjMXK0rKZZdiXBIYu7sDQPs0OKbqPQLFPxrBJ1nSCCNQSQLsebtuOcRqxqZoUkFBSQXUpyQSDYXH55VIY3UAfqfXesSYLHyU8xyawqLua70jX49L+kbLxlSkIlhKykrc1YUNGHTlEmJxstFP7ZS2oFLObEilt6AfeKIRNmapgU1eF1Vv0NIlmzQlWiWSsFPEVB9QLaqUav0FY5v09yuzXIuM6ZK0hUoalU06r2Zw+5p1MLf6Vc9KgdKFCpsHU/Qs9G/zCmXmy3cIemlxye3IekaxWOSpgXSXsTxNZiLeo5c4qxSX+yNhGK+GlYSXBAsTV2Hk3rvEODxQTNcmxejjkBEuGw8lajqmh9IAJapq+kChNBAWbZYqWQUKCkkOVBqGu16s3+Y6xUX8Wyf0cYjN1KdQWEncKsWehu5rEJzILLUJJBcPYBmqHhIv4iGSUn1p29PrGsJPUOIUBLvTmwrGliSWhyLdhp0qWkqYpJqpRLG6jZ2FwPSJZubJVRC1qTpYocA0o4LNfp94reLxgqlVAWFnuXcvAMyYoKL/AKqvfuR7xz/HT2zXIuic3U6ykghJYAMRUEktcgU7E+ioZkQVJKH1hnd6FykBiwoWLVoYRDFEuQUCjBhuPKOsLiv/ANNbKFmY2uSTYvFjgSsnKxnJE4BASFKKQNLkAE3YnbmLQTKzOcjUFKcOxAJUFGhLB6tC6RnK0pTUJK1VUAzprxFhe3YmCJWeLBWdIYEBLFj5F60axiSjLwNFPRMajXFKmkSyEkpKqXbyjIyPezmbWDrZ+Ve4hnJU2o7J26OPeMjI5y6RUFqxZWzU4X9Gt5QOcYQsqjIyMJI0BY3EE8Qo/r1gNUwmpjIyOsUZZwhfEn86RNrKl1NRSMjIrRCaRVdKBifJ2Ip1gfEztaug8rAUjcZEXYfRwjFkduUPcDiy1EpBuSzm9n7jpGRkYzRVBEMzMFOpNCDRjUDlEuBxZMyoBqwpQGhfr5vG4yOfFcSokz2YdSdICTp1EgmtmobGu3KBZOLcEsNQU5NNw1N/eNRkIJcEV9jTKpinDaU6jsLM7fWO8Su/CCa6ja3FRr+bmgjIyOMv3NfQVMypv7hY6dChUtY7bQOvBgqKXILnqHfUzHag9N4yMjlGbZaI5mTIKCsnSQkEBIDUKjvzhYMuStQcni1N2S1TW/S0ZGR3hOVPf/URpA0uWEOSSpIsHbYKr0c2iCZiFTFBixbc7Oze/sIyMj0rycyITj8odjVuZ6xJNxKmfo/1b9/WMjI20gdSWIao9+0MUTSlLgkgh2NfUvUxkZHKXZUFYPRNU6gXe97sLmHS8ulcIDhhdncvqrWm/tGRkeXK2no0iv8AiOXpJA+Vna5eg/eFq5hASOm1GFmjIyPVj3FGX2cypoCiCP8ALj+Y6mkB0Ddy/oYyMjpWwZNnJICUpZhVy9O3OIkYgs3p5UjIyLSB/9k=",
                    "2020-04-18T16:00:41.237", USER_TYPE_INVITE))
            }
            val list: ArrayList<String> = ArrayList()
            list.add("698")
            empty2.add(ContactInfo("Aaa", list.toSet(), "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxITEhUTExMVFhUXGR0bGBgYGBgYFxkYHhoXGBgYGhgdHiggGh0lGxsYITEiJSkrLi4uGh8zODMsNygtLisBCgoKDg0OGxAQGy0lHyUtLS0tLS0tLS0vLS0tLS0tLS0tLS0tNS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLf/AABEIAKYBLwMBIgACEQEDEQH/xAAcAAACAgMBAQAAAAAAAAAAAAAEBQMGAAECBwj/xAA5EAABAgQEBAQFAwMEAwEAAAABAhEAAyExBAUSQSJRYXEGgZGhEzKx0fBCweEUI/EHUmJyFRbCQ//EABkBAQEBAQEBAAAAAAAAAAAAAAABAgMEBf/EACYRAAICAgIBBAMAAwAAAAAAAAABAhEDIRIxUQQTIkEUMmGR0fD/2gAMAwEAAhEDEQA/AOsuOhY0lun7RYsNjSe8V+SRqsSfb1h1IUlnJ8oyDpDqXqLgD3h9LnAJcRXsRjEhMAS84UEFz26RAN8fipa3BMIkT0ILp57wjXmqgtwzE1p7QwxoaXqe7Ewolluws5kirmBcditJ4jSEeUZkhMj4iiXL05MYR5xnTqBdx9IlFst65EuamwdTP1EDKyPgLNSoiqYfP/goTMXUvRAIch79KRYPDviZGISrUyFpPyivCbH9j/MShYArDKkrIqdXpGxjVApCgQ/p5x3N8VYZc1CQx1agVbOCQgddTE+aesLsVjETZjS1cQLM3d/pChY1Cil2NTZo7keI8RLSUhZYWcAntXaEyFq1M7hnBjlU06QokHoTf0iULPUvDuZqGHkHEL1LmKIBYAi7am9HYXHeLCBHjfhvM1/GliZMUJaS7E6gm54Qbbdb1j1fA5lLUkqChTV3YEh25QKGmXGtMA5pncqRLEyYrhJAHnv5CvlEWE8QYebMVLQtykAvTSQQDQ73+sCjTVEK0GFWJ8TYdBmAqdUu4FyXZhzLx3g/EElaX1JF6E7B6+giAaylEUrEsuKGrxx/dbSPhlbP/wAXbV+8AZz/AKkKk4haJaETEAgAks7PqLjmSPTrSoHqqTGlq6x52P8AUtJROWEOUhHwkG6lEHW5B2h5/wC34ZaFrQqiA7HhKjyAPkItgsTl4gxLAVgLLs0RNlpmJsoenSOcZigReJYOAmr7RiUbwIjGgUKmgrD41Cw6C4tGQTCJQnpEshKYnKxZo0kUFBbnG9bQSUDeIFJBNooOQuNkAxKjDjlBKZMKAuVLEa/pusMxKiRMrtFoliReWg7xCrJhtFkCOkZoEOIs8nkpAW3tEU19amBCQPeEOKzb+4gJJFanpFsRKCwkncOesbMFfzDMtAFi9+kK8VPVpJdhGs+S0xTBxuRZ4S47FFQSkU5wRBlMGlIVz+sdTca+mtGqLwuXjeAJP4YXyJpYgC9oAZ4jNXToAIS9XLQnmYl3vQ7wdMwxUlIAdr94GVh/5gACfOJLmO8NilSyopNw3rEk7BGvIb9IHXhVJd4ugckEnzgiVNUghSbiIpUomv0iadJ0ipEGBlhMyCRVqhvRz5RPi8cpTFLAmzWA6QgoRu70juXMWjpyiUC75fI1M7BZena/+If4aTMAfb9vtHncnGzNYKXC2oXsecNZnimajDiQGcOCqpJSQQz7M+3IRhoqZeMfJVNSlKlEhNgbDtCg5aUPwsD6xB4b8YJWUSpzJVpYrP6lUbZg4c1N2i3Kw4UKkxmqLZXMPlTh9zc9Yim5ctJvQcoe6CikAKUpRJJpy5wAqxMsFBYMWvFWxeDJLG46R6Bl8gEKcODHGNyVKUFQDk87RU6BRcNJU7gxrGYxQU8MpuGehATyaF2ZYIOxJp0+saIOMu8Qz9Hw0KIS7huf1ixZBil6FJUd37UaKfk+BK6JeLdk8jQ6SXjDSNWQ51jFjnEeW53MkFCg5S4Kh039ngnOpJJABvbrC+fhtIDOWo3WFCy0D/UA6FAywJmnhUA4CmXUh610U7w18M+NJM2XLTiJiEzzQsCATqUE9Elkg8qjm0U9GVakAlNaQtzWRLkpNAVt8o+ZufQdYCz2uVPlrQlYWClQGk7F7erxLKCCwCgexGxY+9I8ll5pMVgpcuU5CTqSobEEEDoQXML8mxOLlTZU3iIlEhi7aFElQ8yYpT2HOM6w+ECVT5gQFK0jerE2FWYGsNAI8I8S4vEYnUZhJQFEpBfhGpRtazegi3+Hf9RpMvCy0zyfiJUmUwFSjhHxS9AAkuf+pYRpMHpIEbiCdikoQZhPClJUSK0AekI/D/jTC4qSZoUZekErStgUsCo1seEO4jWiFjjIgwuLlzE6pakqS5DguHBYjuDE8Uh8xZbmCFFzccUWPJ/FyVghQ0t8tb+fOPNxMIc+UaRMKTeFGT0LEYmVNEwIIcEuOv794pi1KCi8c4XMFBeo+fON4yc9WZ4AkUgqg3AyfmNB1hZg8UQeYIZusFqxRZm8vKAJsRiwmgNOkbwuIfha8By5SlBzaHGRZWZgKwS4oKUMAFYfAEgkp2N7Wis4kEKKT9Xi9mUzVNq/aEn/AIxK5hSxcRmwJ8uwqiKGh6WiLMcOoAEgtat4sc+ecPI0jS/INz+Y+Qiv5lmKpzaz5xU2AAhqjaJkqJpekQrUGp+WjJcxiaxWCWXPIIMSypzkn62heVx2lXDFaAbKQmtS+xf3iw5Z4txMrWArXqtqBVoLglQcuXDhidwdq1WUtm6i/WCcPNIWD+NyjDQPV8xz+ScNLn0UVKSlaUmqFEOp+QABNbxFOSCgEWUxB5g1HtFFOD0gAVCqv9RDyUqXOly5SAUGWATVgVU1K+0ZaLZaMtlQ3xcoKRp6QkwU8S3+IWBZu9Aw84ZScSDxAuk2MQolzXLNCQpKSWvCfDpQs/3aalMHG8XxOKlqUZeoFTPperc2imeLk6FoTYVNGa8CFhynIpcoOkVMB45ATODHcUgnKc8Cpen/AGoHFzLQlxGMQFpClgEncwoWTeIJw1ywBxJIND1hHnmZlGJSEkEJY8739jCzPM9UVlSNJBNCf9ooPWFOKzErVrarAegaNJCz0TH5wJmFGg/DUpYA3cJ4j22isZlmPxMQtRIBXdWwDW7bRW1T1FQLt3+0SDEHcvChZafDGfJkOFJPwiFEu1VBtDb1AHSvSLNkfiWVOVLkrQy1vVNUu/CALsRvz9vLVzaEA0g7DZktIQoKIUkEAvXyPaDQTPXcXhZenSopGqiXI4ixNOdIpmL8MJ1BRmJCSUgHmSRpHm49YSzs/CsNKQQVTZbh120OSADubCGOGx8soCDMcqUlQfZXJ+d4lUWxl4hk4lEooE2YtDJTo1KZhRNNwB9BFRlCdLdiRqBBbahTcVBYmL5PzKXNniSmaHPJyXZzW3lS0TT8mly0KU6VPUP7eUWLoMT+DPEk3BqQCpRkqUStJa5DOmju4e+8LMd42xaxOlCbN0TFApOspUkJJIYj5Xo4dqbwAsBUwhbkMSD1q1NoSqDlhzo5jSJYvBcRqamojqYz0qOcYpCr9I0Q0zWjtaqHf8vGaD/tPoY5QkvAGpa2Y/jwWmdxd4Ddj+dIlWHIIiMFpyplpAI6dIumUyEIQEppv+dI8yy3MzKIHUX5bxYci8RKmL+GtklRIB9GEZYLh/ShS3c2J6ekblYZAUWDqJcnl2hHi/E8lJWjV8ukAgHUT+q+1okyHxdJWk62QpOonkwLBzzIIiFIfF2UukEfKB52/PWPO9y/aPSs2xyJ8p0qcEAtuLFveKbmWWKQoqZ0tWn40VEEKiXjTx0tFfpGlJjoDkxIiOVJjqWIMHXWJUTmLxx8Lhd/1e0anSylVfzrEBbMFmIKUpPL2p7xBicXoWVS+J3d3A9IRYZZdIiVU2vYl4y4gsq83UJEspT8pDvtuEhuYesPsnxSZ4SywFboBbS70bqz+Ueb/ENnNfSJcNiVy6oUQTyLRmgXTGoVh8UJodTAu9PftA2LzRU9H925G0cZL4gliR8OYklQoLMQTzJfr6xHOllaQpCS3QGkVIEeKmql4c6VkKJABF2fntR4UZhOVNSFE1FDBs5CVpDKCgFVbYgQBOfQa7xoC0F6HaOwKdvWNqktGparvFoGkVBrGJSLxtDOY5BiA7CWF/znGgsmOD9I7TSFA6MytoxK3jSJvOJUAVPSFAmwy/lO4LwbKzZUtZIUSkhlJdxp7HlX1MKtiBEuFBUD+PChY/z/AAstCdUtZIUAbgkDkWhTgknUKUr1BjcyQsBmOmj8qUgvLsZ8KcNaXS1UgDkWI6vBAr61h/rHAmG8FTMtU7iqBV39mgU2aCafQaoM+IQzl+cD4icFMws9fpGIDprHBlUHX8H51hQOMKpOriDj943MZ2FhEy0JsEs1ju/WsaRId29/vB12CFYPeNsUqBB67XFYkkIc1cMdg5iVUgsSC/16UicknQB1zSSSbm/XvGgOVo7WkpLVeNAPtFAZl+YKlqoaMe1Q32g2d4gmKCk0Cbgcmenv7QnXSg3jlrRKBOZ/FYX9o4SgKZy1fbcxwkRiooNhDdY7l4eo7xzhVsXNRDnALSWoLlz0Yn7QsgJ/QqUQAk3g3PcvCVBA+alehvFwwGHQnRpSFMrlYkfaGee5ECApIAffduUSweYoy9k6nqIhSaVoY9C/9fGgECm/8RWM1ytCSW5xqwVpY4o60kQ5VhUlBU1U/WFykE/paAshCtnbtyg5WaTBLVLExTagbnbYVoID+ETxMQHaIlByYULJsPOZvem0GSyn9RptG8DgiUqUwu31iHEyCksYJCzibeBm9zBRQSl4FRFolmzLYH8eMKKtWMJ9Yya4VzpCgdLkvXaMWhmBjlCncGnu0TtwioPa/wDEKFkEpHFEgqaRtKGr9hG8P8zxaFnRksa2jZFRtBGKDEXs8QE1c7RAOsICo3PnBisIAQSBTdRbnzMLsvxaQyjuQxPdoI8X40qUiWlTJQOJt1H+P3gLEspRSA/Vjdvv/IjjE4cUYUNq8tzEs1KkHTtS+9jEsiaVH4YSSDsL2P1jzRbTs6sC+G4ptA6fmtvB6uAKcFJs1adKxyJSmfSehIsY7o5gU41BoOjRqZKUkgqFDVn268oPOFCSSsMnkWfnvUQMJyC7ihLA7W+nWMuX0UhK1eTUHL1jXxlPfa8dzlpo1ohEwbtGaKdTVOASz7mMkp3amx/xE8mWkmj3N69aH19I7mTKnp69o6J60QFVZjGiqO5ssnYvuGt3jhMonY7+14pDTxuUHMSmTQULtbr++0coQEuHHpUecAam07RLJUw+vKNrRw2qeUSjC8Ne5AiAOyrPpslZIrY18g/pSLj4Zz6ZiitMw8SVFSRtppw3qBHm03mz8/vDzw7iTJmJWPmrzseQg0D0fE5mlfAKO49uQir5hhzr0fq6hoYYfFcYOkkmrgWINT+3nDrGyhqTNIYgglxyp5Rnko9sU2UtGVr0KWE0BZXfaJMtkmYWKEsksSWd9/rFpx+ZyU8LEargD1pu13irY3HISr+2rSCTU3NnJG8eefqk9Q2zax+SfO8skkOFhkgcI+r9oRSMvkghRJUCTagZqDqHasT47EKWVaSxLVFm23bbzgRSVmgehuw4rk/SOayZJds3xSGEnNUgFPw0i4sG8oll4eTNQdQU5+U2BPa/TzhemUwS7VazvdmJ9IIMvSXIPTts0Fyj+rZaT7IzloSniodgP/rpCs4FbOzfvDqSEsSSX61J5/naGWW4ITAU3SD/AAY9ePJf7M5TjrRUzl6gAW/OUQ4jCLSpiGLVfbnHpYy2UhDKZQ38t/eE2PyzUvUkhbhnesb92F1ZjhLwUuWHJsCKUDecOcgysTH1OE7K+/SG2U+HyVlkt3/mLFJ8PpSBxEkEuBY943aM7KtPwMmVO+GX77WBPrZoGGEQtbgBId+VIZZvhwuZxuG7e/oIDmqSniT2H53eJZQVeCK1kl9NhAs7Bh2D/m/vDCbOUoAC43+rRmXyFTCrUCSBbev+SYl2BROFgSyR9qfR40K7Of2h5mGVKWSEpNNvy3aK9rKC7VtFotlh/pZZsNRrvQ9tuV4KwOWqTMC0jTcEgip5DvAi8CpMzhClJNEkMSDyUNmp6w4wuVziXVMShLXJBG1E7x8vJOl2emnfQYsS1N8QAtd20u7lxevnESJSNtQFn9LUblYROvItkzk1IFSAA/k52pE/9CAnTqCt3Sqzf8TaOEcy+mbor+ZZPL+GpQ4lCgI0g1NTSr/jRQZzai3lSPV5kuWA4GoEWNiK/hjnDZfh/iFaZICjZSnIFNTAGgTSPQvVcO1ZiWO2eXSsMaOQBW7flxB2CydeIUyQEgC5sz0sOf1Eejz/AA5hZiwualQZ3APColjV6g0NQ0EyckkUXJmJlAONP6SKUAehoziMv18fBFiZTcB4Z0LZagoCwSPm3ufpDCZkkhytSQRydkAtvzAH7Q6xGDmOQSNnYMm4oD+fQRLMwSiggrAFkhied2g87auzagvAmwwRNckAdUirAOHINTsHG0TnJZRAStKykmoB0gVO1w55Ex3hslKCn4RB1Bi5IpQEvff2jr+mW/IC6gDVqUHXl1vGea+mK8nUrI8Kk6vgAqJDuCsAuADen4TAGa+G5KnCGlrJ2StSSeT339oazZ+kqSkqcbUv6+/8wMcQV/Ma7VHs9PpbrGIzn2pMjS8CAeE5hWNC3DVJoQRfhb5eR7RzifDk4JcAKPdIYedPwRZcEheopBSpns9ObsKV5XiSblQKtapnCNknoWIPfbpeO35U090T219HnKcvVq0kHWSAEFqvXe/eLVIy9EgJOlMxe5uBYEDoPKLAcDKYL0grSGQVhJI5sKVLX6mAZ0gI1F2JqR3FQKc/wRMnqXkpLRFj4kc7NTJTpSGP6RpejBvIQrxebzFoLrKuwa3Y1NxA+NnEEquCWc197wFh8Ks9BZyQPrfyrFx4Yr5PsrkyT+qWSFBqUrTYiBZ6QVHVa+x7t1gxGXcOosXHCARYXUWen2tEuiWmXqSlXEwJUQ3NVGDVo/SN6XRkDRPSCwSyQxZ3839YlRiWNE0NjvaMIUVUSztdn23NYgxEx2DBnb8fvG0rBLt1cem7PtE0/UwJLim/38oX45agAxF3+0blTlNxJ07gtRqc7xa+xYylsxZId97WEF4HEKSXSdNnHP8AP3hXInHTVyOTsxt5/nl2mfcU+tvrCrKmPfiqcuos29exjFzE1Zhbie9qs+72ELsPPcEFi9BWtKwFjFFJABp0q1nc945e3vwa5Fpw2LWlgCFbuzParOYf5NjUlxMsRc/SKBgMatmBc1+j926QXKzBRLue35btHRco9E+L7LFnWTLmznSGTWpFA2z/ALQj/wDAT6y9NXYHZq8TnaGWH8RJAAXrOzA07t+Wgo5slgAVW/3V++5ifkZb6Rn2o+RZgPD60JIXc0cEME9zzrbpD/DZeqUEslLJHzfMS70Z7dYRjN1OyiVgFr/nKCVY1EwnjUFHc1A6O0ZefN/DSxwGapiVFZBB1bCgTYXdhSKlP8OutlzAmWHIUgOroGLCvPpzh5hMvqSZiSTyFPKt/wA79ryuaU6Zagp72S4vUkttzvGZepkl3Q9teCvYFcwktb8dm5dbVhhPWlQSFcQTyO/lesIMMTLSQVAvsCVMdvb9oOw88E6jatujVAMacU2VMmOarD7EWAHoAP3iORmM1wtjoFaCj3BJ5kxOpEvqC901YOHA69eoiZWZAMJZXoAA0qZQpRjyttHOVLSiCbD4n43GyAonhcqIKn5VDPsKRNMxa5adLOQ5ob1IDbjdwOkAlDAgOkByyaBiR67bwOoahpLC5Sp2FuJw1i+3KOLim/4WyLHY5baSo1N2vV2PvE+DlTlJo7E36ULPs1/WEE+eQ6C6WPQ9DXfeGOVz5iW0lA1UBUTffsAOd3j0ShUdGE9lxTN0gIcqowYksw9LtEczFlLsaWU7PX+D7QimYsjVxAlmcEdi4Y0c0rEC8UdJClBjcfqfkzDZvWOUcWjfIsKs24eElqgH0JMbk58wJABVY0v582eKrOmkUcAMWYu1uW+3rEeKzDhbWph+c4r9OmqJzZZZuMlqZQBNzUgFJNw3ne0BHCKW5SUiwZ2p/g+0JsDigX5gU69zyDflYe5XjgSKBu21YvDgtBO+yfAYeYngQCSWJVQFhs3Y7n6R3NmrCXLsCw2B7v8Am8HKxKZZUoGofT81H6WLAn0MK8ROOmlXpUMQ5Ad9xYejxmK5bZro4GLUb1FRyBBoz/l43i5RDuBRrhq97nvEmUIClEkuzOAQQ/I26A9jsHgvFT/iHQh6i6hV7khNNrPyjTVPQW0VfOSQyiA4DlqFhsAQGL9r1gPB1WNRD8+Qqatc1v8A4i1ZngUhDTGZuGxNqbkE3FD5xW8nlKClq0pQQksSRQhSW8qnavvHaEk4tGJKmESgkDWl0kgggsFbUAFWq47mF+bqUCwB0iwsGpcWf8eGYIC0JDKUVaVqBTwukBJ0gAFq7lg0K8wAUVEkMCNzuFEgXa49o3FbsywWqgW1Pd37fSJVsjQClyS3PsWjeHwswkgJVQEltgwLPsbsIgxOFmJKdYN6cyH6VFfpGtENLmjWDSjXqCXO3SC8TOJSQeIB6BISNuV/5gBKElQdKrgtzu21qwVjMSSDya1KH8aDSbQNT8RqBA+YUblv+wgdWI0lgY1KXVRIYuBQM/M0iGWCtVBz2BYV/iNpJAY4TF8VLippQgVNq7QVOzJm1BKnO4uT0EL5SNJ1EjkGDnlbz5xpGMVZAqdlBJG9y37xzcU+hY7wuKQoafggACqhqSa0BJJavWI14MpfSpBYPVQ7gA236wpxeIWWTqJAsBYnsmkN5WT4j4WskKo4Rq42H/A1A7e0c38O3VmlbME6WlNfm3JJoe4NulabxK7Ci0noKvRuVqdbeiPFzSo19j+zwWFLIdP6mFBxE3Yegtb0jpx0SwlQCXJd2G+3NvasRyMYNtXR27XgzE5bMlyyVJ2Brtcjrfz7RXxMYlw9LVbf7CKkpIPQ+l5kUsSTzu1+XWGuDzYqOkJBJ5B1WerXMVKbOZ1NyblflvFp8PZ7LQgJXITqlk/3UnTMZT3JBq9Ox9eOeHxtRs1F7Kvg8KTOloJ0gLertoBDs7Evb9ouJkYJRUUy1JLBgCdBNh0Fvr0iuTsIpMz/AKij17/uIFl4pYuqidjy2B9oO5bTC0XWRhJYlkBIUSCH87s7pYV8oXS5Alk6SD003fhcGuz+kJsNj1K4nI+24Zuw7QZlmJd0rsbfV+Qbrz9eXCS7Zq0FpUVGw9nI8t/uIDxcol6WFBYk2AHKkc/1CUkp22IZ+pB5Vv06RF/UDUku79d7b+fONqLI2Byslnq4gHd3FC16kXg3D4VaEqSSzgF9JpWlfywhjhZiD+k3ajUNQfy8Ga5CSVqAWovRZUQBVrdOfKMzyy6aM0VhBYFT17DnvzhnIwhUEqLOQ7/qYpLlgeZFTyg2RKwqnWlLEPwhZICnFWVf6RNiESllkqUVHcK+b5aEUY9usSWf6SFFazCSlMpysggBw1jyf8tAWDxWmm53O38/aLuvKJEwKBIIUzhJFW5g7PvAeK8NSpcwKly9blwCXZg5BSaEeXKLD1MGqYcWIMGqWC5dRdwBt5bw/wAOiYqWFS0gBhw6uIVs17bRDjc4VL1JMtLkAl0izUFns8Dozda1hJeWFF3qWsnUz8wRCXKW6KtFrRhdKePSV/K6VNTZLDfYRiUISjQkAGtya2a/YFmvzivz501Ck6ZqTwkk+ZAoakkNEWBxsxQ4lEpfsCaHa28eeKktpm7Q9w6Phgh0jUQVaqVq1fN4m0AB1GlxegFWHqH7Meq5SpiwUhLCjKZVn5tf7xEwSkOs3BVXYVHY/eN8r77HQROSluFRUz0mHV3enJx9YVJw6UgmaVJQAzJ3H/bvyPnDjDzZaks1eZ3Luo12o/eO5okqI0hJa4CqLDMkEc77CqerxlZnHTK0mV7J8HrmEqSlISOIJJYAt+o1JNa08obzcswx06kkEL1EnUQrYgjcDh9IJXipRSlLaDsD8rDYD19DyhaUqKGCgnYJLkq5Nyt+b7eRz23RmkibEzgs6HDEs5DaWLbVFrNvtHMzCS5QdROttJNxp2YDp+ViD4WJQAlTGlr8VwBuHFWpHWLUSE637g0rVIO3Uc4i8J6B2mTKSdRqSPmIrQ0SACGF/vEGMyWVMImhY0uBpYhxdTkVDl6gFnjidhVEatXJKaF6guTVqW84mlrmS9RI4QLFmfYgG1eUatr9XsjNycmwqErSkrUVKcKLOAxBqzsxPpCHC5Li1khMpSdRuHAqRXsL02h8qcnUklVCCVJtpP6aE1c8q184YTc2TLlsCvk7gnehs3KL704/2yUmVDE5LPlKPxkG2rY7p1Hk4BBP+YXzVivCoC+9d7GnP0i4YLPlFJQsFmYKaocMNJvt7dBEWBwmGnqTLKlqOnTrcAgaRUDkL1e8dY52v3X+CcfBW8pT8SchBUUg/qDahZr07nk8eh5dI0yZaV8KxVgHLv357fWEmG8MGRiErTN1ISS7gak1IHe4L0horFsqtVGnWzO3d/WOHqJqbqO0bgqK/m/hpRmTSVoR/tAsFKYpdvlHOn3g/BYIYYkAFbq4FFnLCtma7F+1WgiZjfmZ1EXWn9NwPdRvDPCyaomqS50DS9Q4u7hhXT9qRmWaajT6FKxYormoZKFEqoVPRqOH5bf4g2R4Xko0FQSvhKWUEqSNRB1AV9Otnhh/VKIIUSpO7JGkB6DT5wLJSFf3EzCA5oSAwDg+VC305Pe1o1QuPhsJA0olq6HU9iaalF7nz7GMOWISvSlHCAOFzpSWBIcF3fdzyjc9CVmj2FjVQpXZ6cutommYyYgAKFFOS44WDNbeOiytkdFTkqJDBbHvQ15tsG9YIxKRpS+lzQkXuCab2/iKqjFl0vT15QScf58ulG+0el4nZzTHsySkpCEMRRiPoSd35QHhZxEw6qBLOxFGZm72vAkvMWYNR35dr/tE82ZVw383DRFFrTKaxhXqJu7HrWjHqPtA6pqgHUKCg73bpSGK8vX82oDU5IOx2Ybn+Y6nSZawEkHUdyXrz6RVJEoOE9DhZUKjiTud/m+nOBMznJYkVQGpRw/W5rAEmRpcFdA9N25B+sC4mdtYG/Pl5mMLHsNh2ExyZZKpYKuRLAioIAbf6vDHDZikV4akupqpJOzXpt0iqf1BFHp6Xb88o4E9lMS77j7Rt4UzNlvwWaBCipQKVCjgtQk1a38wVJ8RMSkS3S/zAl3apDv6RVk5ikhlp1aejBqXb8vEKZykqJT8tN2d6sfvHN4E+0XkWrOHmhRAqQK2Z+dP+u7/AEgLATDLUn4j1o4NvWBEZiQpJf8ATWxHKx7xPlS0zidaiwIIYhzsCzNzHnE4uMafRbJsfmSFK0pLB6que37dKxn/AJaYiik8RICioB3cNT9ukGysDIw0xSyp0sfJ6JA7PeCJ+Kw890iho7iqgLfVR5veOdx1SteSkWC8QguhRZA383s+5L0gtGNlr6lYoW/TRjShqPpCDMcuSs/2iygGCDQqU48mFfXkBAKV6NKDr1iigSGo5J6Dv6xVijLcRbG+YYpQoAXSKtta5Hl7QLIxzKcqZxUl70P1+t4IwGdKQXKVKLB22fc9+UEYvL8OErCkq/uFwpLnQfm0kEs9Ghaj8ZIEWFzHW4+ZwSAzkFrDy3jvBbzJiiksyQfmfY9gK+bQJgcMMKr4hXqUKNRmofcQLjMXK0rKZZdiXBIYu7sDQPs0OKbqPQLFPxrBJ1nSCCNQSQLsebtuOcRqxqZoUkFBSQXUpyQSDYXH55VIY3UAfqfXesSYLHyU8xyawqLua70jX49L+kbLxlSkIlhKykrc1YUNGHTlEmJxstFP7ZS2oFLObEilt6AfeKIRNmapgU1eF1Vv0NIlmzQlWiWSsFPEVB9QLaqUav0FY5v09yuzXIuM6ZK0hUoalU06r2Zw+5p1MLf6Vc9KgdKFCpsHU/Qs9G/zCmXmy3cIemlxye3IekaxWOSpgXSXsTxNZiLeo5c4qxSX+yNhGK+GlYSXBAsTV2Hk3rvEODxQTNcmxejjkBEuGw8lajqmh9IAJapq+kChNBAWbZYqWQUKCkkOVBqGu16s3+Y6xUX8Wyf0cYjN1KdQWEncKsWehu5rEJzILLUJJBcPYBmqHhIv4iGSUn1p29PrGsJPUOIUBLvTmwrGliSWhyLdhp0qWkqYpJqpRLG6jZ2FwPSJZubJVRC1qTpYocA0o4LNfp94reLxgqlVAWFnuXcvAMyYoKL/AKqvfuR7xz/HT2zXIuic3U6ykghJYAMRUEktcgU7E+ioZkQVJKH1hnd6FykBiwoWLVoYRDFEuQUCjBhuPKOsLiv/ANNbKFmY2uSTYvFjgSsnKxnJE4BASFKKQNLkAE3YnbmLQTKzOcjUFKcOxAJUFGhLB6tC6RnK0pTUJK1VUAzprxFhe3YmCJWeLBWdIYEBLFj5F60axiSjLwNFPRMajXFKmkSyEkpKqXbyjIyPezmbWDrZ+Ve4hnJU2o7J26OPeMjI5y6RUFqxZWzU4X9Gt5QOcYQsqjIyMJI0BY3EE8Qo/r1gNUwmpjIyOsUZZwhfEn86RNrKl1NRSMjIrRCaRVdKBifJ2Ip1gfEztaug8rAUjcZEXYfRwjFkduUPcDiy1EpBuSzm9n7jpGRkYzRVBEMzMFOpNCDRjUDlEuBxZMyoBqwpQGhfr5vG4yOfFcSokz2YdSdICTp1EgmtmobGu3KBZOLcEsNQU5NNw1N/eNRkIJcEV9jTKpinDaU6jsLM7fWO8Su/CCa6ja3FRr+bmgjIyOMv3NfQVMypv7hY6dChUtY7bQOvBgqKXILnqHfUzHag9N4yMjlGbZaI5mTIKCsnSQkEBIDUKjvzhYMuStQcni1N2S1TW/S0ZGR3hOVPf/URpA0uWEOSSpIsHbYKr0c2iCZiFTFBixbc7Oze/sIyMj0rycyITj8odjVuZ6xJNxKmfo/1b9/WMjI20gdSWIao9+0MUTSlLgkgh2NfUvUxkZHKXZUFYPRNU6gXe97sLmHS8ulcIDhhdncvqrWm/tGRkeXK2no0iv8AiOXpJA+Vna5eg/eFq5hASOm1GFmjIyPVj3FGX2cypoCiCP8ALj+Y6mkB0Ddy/oYyMjpWwZNnJICUpZhVy9O3OIkYgs3p5UjIyLSB/9k=",
                "2020-04-18T16:00:41.237", USER_TYPE_CONNECTION))
            return empty2
            //return contacts
        }
    }
}