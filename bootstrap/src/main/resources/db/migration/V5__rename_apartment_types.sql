-- Rename ApartmentType enum values to match new naming convention
UPDATE apartments SET apartment_type = 'ONE_BEDROOM'      WHERE apartment_type = 'ONE_BR';
UPDATE apartments SET apartment_type = 'TWO_BEDROOM'      WHERE apartment_type = 'TWO_BR';
UPDATE apartments SET apartment_type = 'TWO_BEDROOM_PLUS' WHERE apartment_type = 'TWO_BR_PLUS';
UPDATE apartments SET apartment_type = 'THREE_BEDROOM'    WHERE apartment_type = 'THREE_BR';
UPDATE apartments SET apartment_type = 'THREE_BEDROOM_PLUS' WHERE apartment_type = 'THREE_BR_PLUS';
