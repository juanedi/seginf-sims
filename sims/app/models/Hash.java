package models;

import play.libs.Crypto.HashType;

/**
 * Tipos de hash.
 * Similar a {@link HashType}, pero para tener uno propio de
 * nuestor modelo independiente de play.
 * 
 * @author Juan Edi
 * @since May 29, 2012
 */
public enum Hash {

    MD5,
    SHA1,
    SHA256,
    SHA512;
    
}
