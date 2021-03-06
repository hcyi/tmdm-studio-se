/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.mdm.repository.model.mdmserverobject.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.mdm.repository.model.mdmserverobject.MdmserverobjectPackage;
import org.talend.mdm.repository.model.mdmserverobject.WSResourceE;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>WS Resource E</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.mdm.repository.model.mdmserverobject.impl.WSResourceEImpl#getFileExtension <em>File Extension</em>}</li>
 *   <li>{@link org.talend.mdm.repository.model.mdmserverobject.impl.WSResourceEImpl#getFileContent <em>File Content</em>}</li>
 *   <li>{@link org.talend.mdm.repository.model.mdmserverobject.impl.WSResourceEImpl#getImageCatalog <em>Image Catalog</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WSResourceEImpl extends MDMServerObjectImpl implements WSResourceE {

    /**
     * The default value of the '{@link #getFileExtension() <em>File Extension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFileExtension()
     * @generated
     * @ordered
     */
    protected static final String FILE_EXTENSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFileExtension() <em>File Extension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFileExtension()
     * @generated
     * @ordered
     */
    protected String fileExtension = FILE_EXTENSION_EDEFAULT;

    /**
     * The default value of the '{@link #getFileContent() <em>File Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFileContent()
     * @generated
     * @ordered
     */
    protected static final byte[] FILE_CONTENT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFileContent() <em>File Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFileContent()
     * @generated
     * @ordered
     */
    protected byte[] fileContent = FILE_CONTENT_EDEFAULT;

    /**
     * The default value of the '{@link #getImageCatalog() <em>Image Catalog</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImageCatalog()
     * @generated
     * @ordered
     */
    protected static final String IMAGE_CATALOG_EDEFAULT = "Default_Catalog";

    /**
     * The cached value of the '{@link #getImageCatalog() <em>Image Catalog</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImageCatalog()
     * @generated
     * @ordered
     */
    protected String imageCatalog = IMAGE_CATALOG_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected WSResourceEImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return MdmserverobjectPackage.Literals.WS_RESOURCE_E;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFileExtension(String newFileExtension) {
        String oldFileExtension = fileExtension;
        fileExtension = newFileExtension;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, MdmserverobjectPackage.WS_RESOURCE_E__FILE_EXTENSION, oldFileExtension, fileExtension));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public byte[] getFileContent() {
        return fileContent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFileContent(byte[] newFileContent) {
        byte[] oldFileContent = fileContent;
        fileContent = newFileContent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, MdmserverobjectPackage.WS_RESOURCE_E__FILE_CONTENT, oldFileContent, fileContent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getImageCatalog() {
        return imageCatalog;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setImageCatalog(String newImageCatalog) {
        String oldImageCatalog = imageCatalog;
        imageCatalog = newImageCatalog;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, MdmserverobjectPackage.WS_RESOURCE_E__IMAGE_CATALOG, oldImageCatalog, imageCatalog));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case MdmserverobjectPackage.WS_RESOURCE_E__FILE_EXTENSION:
                return getFileExtension();
            case MdmserverobjectPackage.WS_RESOURCE_E__FILE_CONTENT:
                return getFileContent();
            case MdmserverobjectPackage.WS_RESOURCE_E__IMAGE_CATALOG:
                return getImageCatalog();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case MdmserverobjectPackage.WS_RESOURCE_E__FILE_EXTENSION:
                setFileExtension((String)newValue);
                return;
            case MdmserverobjectPackage.WS_RESOURCE_E__FILE_CONTENT:
                setFileContent((byte[])newValue);
                return;
            case MdmserverobjectPackage.WS_RESOURCE_E__IMAGE_CATALOG:
                setImageCatalog((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case MdmserverobjectPackage.WS_RESOURCE_E__FILE_EXTENSION:
                setFileExtension(FILE_EXTENSION_EDEFAULT);
                return;
            case MdmserverobjectPackage.WS_RESOURCE_E__FILE_CONTENT:
                setFileContent(FILE_CONTENT_EDEFAULT);
                return;
            case MdmserverobjectPackage.WS_RESOURCE_E__IMAGE_CATALOG:
                setImageCatalog(IMAGE_CATALOG_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case MdmserverobjectPackage.WS_RESOURCE_E__FILE_EXTENSION:
                return FILE_EXTENSION_EDEFAULT == null ? fileExtension != null : !FILE_EXTENSION_EDEFAULT.equals(fileExtension);
            case MdmserverobjectPackage.WS_RESOURCE_E__FILE_CONTENT:
                return FILE_CONTENT_EDEFAULT == null ? fileContent != null : !FILE_CONTENT_EDEFAULT.equals(fileContent);
            case MdmserverobjectPackage.WS_RESOURCE_E__IMAGE_CATALOG:
                return IMAGE_CATALOG_EDEFAULT == null ? imageCatalog != null : !IMAGE_CATALOG_EDEFAULT.equals(imageCatalog);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (fileExtension: ");
        result.append(fileExtension);
        result.append(", fileContent: ");
        result.append(fileContent);
        result.append(", imageCatalog: ");
        result.append(imageCatalog);
        result.append(')');
        return result.toString();
    }

    @Override
    public int getType() {
        return 23;
    }

    @Override
    public String getUniqueName() {
        return getName() + "." + getFileExtension();
    }

} // WSResourceEImpl
